package net.smileycorp.deeperdepths.common.world.chambers.spawners;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialSpawner;
import net.smileycorp.deeperdepths.common.entities.DeeperDepthsEntities;
import net.smileycorp.deeperdepths.common.entities.EntityBogged;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;
import net.smileycorp.deeperdepths.common.world.base.WorldGenCustomStructure;

import java.util.Random;

/**
 * Small structure generation specifically for the Trial Spawners to spawn appropiately with there tile
 */
public class WorldGenTrialSpawnerType extends WorldGenCustomStructure {


    public WorldGenTrialSpawnerType(String structureName) {
        super("trial_chambers/spawn/" + structureName);

    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        this.generateStructure(worldIn, position.add(-1, -1, -1), Rotation.NONE);
        return true;
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand) {
        //Here's where will we handle specific data blocks for what type of mobs will be in the spawner
        //Skeleton
        if(function.startsWith("skeleton")) {
            //Spawner
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            // the data for the spawner?
            if(tileEntity instanceof TileTrialSpawner) {
                EntitySkeleton skeleton = new EntitySkeleton(worldIn);
                ((TileTrialSpawner)tileEntity).setCachedEntity(skeleton);
                //there gotta be something I'm doing wrong here
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(3).setTotalEntities(7).setSpawnRange(4).setTicksBetweenSpawn(40)
                        .setEntities(ImmutableMap.of(skeleton.getEntityData().getCompoundTag("id"), 1)));

                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.ACTIVE);

            }
        } else if (function.startsWith("zombie")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).setCachedEntity(new EntityZombie(worldIn));
            //    ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.ACTIVE);
            }
        } else if (function.startsWith("stray")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).setCachedEntity(new EntityStray(worldIn));
              //  ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.ACTIVE);
            }
        } else if (function.startsWith("bogged")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).setCachedEntity(new EntityBogged(worldIn));
              //  ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.ACTIVE);
            }
        }  else if ((function.startsWith("silver_fish"))) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).setCachedEntity(new EntitySilverfish(worldIn));
             //   ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.ACTIVE);
            }
        } else if (function.startsWith("spider")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).setCachedEntity(new EntitySpider(worldIn));
           //     ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.ACTIVE);
            }
        }else if (function.startsWith("breeze")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).setCachedEntity(new EntityBreeze(worldIn));
             //   ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.ACTIVE);
            }
        }
    }
}
