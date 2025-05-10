package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.capabilities.VibrationHandler;
import com.deeperdepths.config.SensorSoundHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileSculkSensor extends TileEntity implements ITickable, VibrationHandler.Listener {
    
    private int cooldown, frequency;
    private boolean hasSignal;
    
    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).add(this);
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).remove(this);
    }
    
    @Override
    public boolean canHear(SoundEvent sound, double x, double y, double z) {
        if (hasSignal || cooldown > 0) return false;
        if (getDistanceSq(x, y, z) > 64) return false;
        return true;
    }
    
    @Override
    public void reactToSound(SoundEvent sound, double x, double y, double z) {
        cooldown = 40;
        frequency = SensorSoundHandler.getSoundFrequency(sound);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }
    
    @Override
    public void update() {
        if (cooldown-- > 0) return;
        if (cooldown > 0) return;
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                DeeperDepthsSoundEvents.SCULK_SENSOR_CLICKING_STOP, SoundCategory.BLOCKS, 1, 1);
        world.markBlockRangeForRenderUpdate(pos, pos);
        frequency = 0;
    }
    
}
