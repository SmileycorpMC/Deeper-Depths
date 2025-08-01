package com.deeperdepths.common.capabilities;

import com.deeperdepths.common.Constants;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;

public class LightingRods extends WorldSavedData {
    
    public static final String DATA = Constants.MODID + "_lightning_rods";
    
    private final HashSet<BlockPos> positions = Sets.newHashSet();
    
    public LightingRods(String data) {
        super(data);
    }
    
    public void add(BlockPos pos) {
        positions.add(pos);
        markDirty();
    }
    
    public void remove(BlockPos pos) {
        positions.remove(pos);
        markDirty();
    }
    
    public Optional<BlockPos> getClosest(WorldServer world, BlockPos pos, int radius) {
        int rsqr = radius * radius;
        return positions.stream().filter(p -> p.distanceSq(pos) <= rsqr).filter(world::canBlockSeeSky)
                .min(Comparator.comparingDouble(p -> (int) p.distanceSq(pos)));
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("positions")) {
            positions.clear();
            for (NBTBase nbt : compound.getTagList("positions", 10))
                positions.add(NBTUtil.getPosFromTag((NBTTagCompound) nbt));
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList positions = new NBTTagList();
        for (BlockPos pos : this.positions) positions.appendTag(NBTUtil.createPosTag(pos));
        compound.setTag("positions", positions);
        return compound;
    }
    
    public static LightingRods get(WorldServer world) {
        LightingRods data = (LightingRods) world.getMapStorage().getOrLoadData(LightingRods.class, DATA);
        if (data == null) {
            data = new LightingRods(DATA);
            world.getMapStorage().setData(DATA, data);
        }
        return data;
    }
    
}
