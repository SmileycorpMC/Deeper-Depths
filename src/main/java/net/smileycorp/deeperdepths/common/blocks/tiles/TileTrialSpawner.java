package net.smileycorp.deeperdepths.common.blocks.tiles;

import net.minecraft.tileentity.TileEntityMobSpawner;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;

public class TileTrialSpawner extends TileEntityMobSpawner {
    
    private EnumTrialSpawnerState state = EnumTrialSpawnerState.INACTIVE;
    private boolean isOminous;
    
    public EnumTrialSpawnerState getState() {
        return state;
    }
    
    public boolean isOminous() {
        return isOminous;
    }
    
}
