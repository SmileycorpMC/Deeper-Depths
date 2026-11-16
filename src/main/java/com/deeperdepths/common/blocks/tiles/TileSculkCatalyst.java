package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.capabilities.SculkCatalysts;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileSculkCatalyst extends TileEntity implements ITickable
{
    public final List<SculkCharge> activeSpreaders = new ArrayList<>();

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (world instanceof WorldServer) SculkCatalysts.getInstance((WorldServer) world).add(this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (world instanceof WorldServer) SculkCatalysts.getInstance((WorldServer) world).remove(this);
    }

    @Override
    public void update()
    {
        if (world.isRemote) return;

        Iterator<SculkCharge> iter = activeSpreaders.iterator();

        while (iter.hasNext())
        {
            SculkCharge spreader = iter.next();
            boolean stillAlive = spreader.update();

            if (!stillAlive) iter.remove();
        }
    }

    public void bloom(EntityLivingBase entity, int experience) {
        activeSpreaders.add(new SculkCharge(world, entity.getPosition().down(), experience));
        world.playSound(null, pos, DeeperDepthsSoundEvents.SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 2.0F, 0.6F + world.rand.nextFloat() * 0.4F);
    }

}