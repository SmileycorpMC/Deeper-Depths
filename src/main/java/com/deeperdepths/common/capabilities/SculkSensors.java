package com.deeperdepths.common.capabilities;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.config.SensorSoundHandler;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashSet;

public class SculkSensors extends WorldSavedData {
    
    public static final String DATA = Constants.MODID + "_sculk_sensors";
    
    private final HashSet<BlockPos> positions = Sets.newHashSet();
    
    public SculkSensors(String data) {
        super(data);
    }
    
    public void add(BlockPos pos) {
        positions.add(pos);
    }
    
    public void remove(BlockPos pos) {
        positions.remove(pos);
    }
    
    public void playSound(SoundEvent sound, double x, double y, double z) {
        int frequency = SensorSoundHandler.getSoundFrequency(sound);
        if (frequency == 0) return;
        DeeperDepths.info("Sound " + sound.getRegistryName()  + " with frequency " + frequency + " at " + new Vec3d(x, y, z));
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
    
    public static SculkSensors get(WorldServer world) {
        SculkSensors data = (SculkSensors) world.getMapStorage().getOrLoadData(SculkSensors.class, DATA);
        if (data == null) {
            data = new SculkSensors(DATA);
            world.getMapStorage().setData(DATA, data);
        }
        return data;
    }
    
}
