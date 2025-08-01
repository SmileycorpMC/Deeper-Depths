package com.deeperdepths.common.world.chambers.spawners;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import com.deeperdepths.common.blocks.tiles.TileTrialSpawner;
import com.deeperdepths.common.world.base.WorldGenCustomStructure;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
                //there gotta be something I'm doing wrong here
                ((TileTrialSpawner)tileEntity).modifyConfigs(config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(config.isOminous() ? new TileTrialSpawner.Entry(new ResourceLocation("skeleton")).setRangedEquipment()
                        : new TileTrialSpawner.Entry(new ResourceLocation("skeleton")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        } else if (function.startsWith("zombie")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(config.isOminous() ? new TileTrialSpawner.Entry(new ResourceLocation("zombie")).setMeleeEquipment()
                                : new TileTrialSpawner.Entry(new ResourceLocation("zombie")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        } else if (function.startsWith("stray")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(config.isOminous() ? new TileTrialSpawner.Entry(new ResourceLocation("stray")).setRangedEquipment()
                                : new TileTrialSpawner.Entry(new ResourceLocation("stray")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }

        } else if (function.startsWith("bogged")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(config.isOminous() ? new TileTrialSpawner.Entry(Constants.loc("bogged")).setRangedEquipment()
                                : new TileTrialSpawner.Entry(Constants.loc("bogged")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        }  else if ((function.startsWith("silver_fish"))) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(new TileTrialSpawner.Entry(new ResourceLocation("silverfish")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        } else if (function.startsWith("spider")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(new TileTrialSpawner.Entry(new ResourceLocation("spider")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        }else if (function.startsWith("breeze")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs(config -> config.setSimultaneousEntities(1).setTotalEntities(2).setSpawnRange(4).setTicksBetweenSpawn(60).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(new TileTrialSpawner.Entry(Constants.loc("breeze")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        } else if (function.startsWith("cave_spider")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(new TileTrialSpawner.Entry(new ResourceLocation("cave_spider")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        } else if (function.startsWith("slime")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(new TileTrialSpawner.Entry(new ResourceLocation("slime")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        } else if (function.startsWith("husk")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) {
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(config.isOminous() ? new TileTrialSpawner.Entry(new ResourceLocation("husk")).setMeleeEquipment()
                                : new TileTrialSpawner.Entry(new ResourceLocation("husk")), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            }
        } else if (function.startsWith("baby_zombie")) {
            worldIn.setBlockState(pos, DeeperDepthsBlocks.TRIAL_SPAWNER.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileTrialSpawner) try {
                NBTTagCompound nbt = JsonToNBT.getTagFromJson("{id: \"minecraft:zombie\", IsBaby: 1b}");
                ((TileTrialSpawner)tileEntity).modifyConfigs( config -> config.setSimultaneousEntities(2).setTotalEntities(6).setSpawnRange(4).setTicksBetweenSpawn(40).setSimultaneousEntitiesPerPlayer(1)
                        .setEntities(ImmutableMap.of(config.isOminous() ? new TileTrialSpawner.Entry(nbt).setMeleeEquipment()
                                : new TileTrialSpawner.Entry(nbt), 1)));
                tileEntity.setWorld(worldIn);
                ((TileTrialSpawner)tileEntity).setState(EnumTrialSpawnerState.WAITING);
                tileEntity.setPos(pos);
            } catch (Exception e) {
                DeeperDepths.error("Unable to set spawner baby_zombie", e);
            }


        }
    }
}