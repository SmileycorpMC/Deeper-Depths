package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.capabilities.SculkCatalysts;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.*;

public class TileSculkCatalyst extends TileEntity implements ITickable
{
    private static final int maxSpreaders = 32;

    /** Currently active charges. */
    private final List<SculkCharge> activeSpreaders = new ArrayList<>();
    /** Charges to be added to the tile entity. They are queued as to prevent shit getting changed mid-tick! */
    private final List<SculkCharge> pending = new ArrayList<>();

    @Override
    public void setWorld(World world)
    {
        super.setWorld(world);
        if (world instanceof WorldServer) SculkCatalysts.getInstance((WorldServer) world).add(this);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (world instanceof WorldServer) SculkCatalysts.getInstance((WorldServer) world).remove(this);
    }

    @Override
    public void update()
    {
        if (world.isRemote) return;

        if (!pending.isEmpty())
        {
            activeSpreaders.addAll(pending);
            pending.clear();
        }

        if (activeSpreaders.isEmpty()) return;

        Map<BlockPos, SculkCharge> survivors = new LinkedHashMap<>();

        for (SculkCharge charge : activeSpreaders)
        {
            if (!charge.update(world)) continue;

            BlockPos key = charge.getPos().toImmutable();
            SculkCharge existing = survivors.get(key);

            if (existing != null) existing.mergeCharges(charge);
            else if (survivors.size() < maxSpreaders) survivors.put(key, charge);
        }

        activeSpreaders.clear();
        activeSpreaders.addAll(survivors.values());

        if (activeSpreaders.isEmpty() || world.getTotalWorldTime() % 20 == 0)
        { this.markDirty(); }
    }

    public void bloom(EntityLivingBase entity, int experience)
    {
        this.pending.add(new SculkCharge(pos, entity.getPosition().down(), experience));
        world.playSound(null, pos, DeeperDepthsSoundEvents.SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 2.0F, 0.6F + world.rand.nextFloat() * 0.4F);
        this.markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        NBTTagList list = new NBTTagList();
        for (SculkCharge charge : activeSpreaders) list.appendTag(charge.writeToNBT());
        for (SculkCharge charge : pending) list.appendTag(charge.writeToNBT());
        compound.setTag("Charges", list);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        activeSpreaders.clear();
        pending.clear();

        NBTTagList list = compound.getTagList("Charges", 10);
        for (int i = 0; i < list.tagCount(); i++)
        { activeSpreaders.add(SculkCharge.fromNBT(this.pos, list.getCompoundTagAt(i))); }
    }
}