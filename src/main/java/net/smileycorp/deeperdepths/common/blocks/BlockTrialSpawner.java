package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialSpawner;

import javax.annotation.Nullable;

public class BlockTrialSpawner extends BlockTrial {
    
    public BlockTrialSpawner() {
        super("Trial_Spawner");
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileTrialSpawner();
    }
    
}
