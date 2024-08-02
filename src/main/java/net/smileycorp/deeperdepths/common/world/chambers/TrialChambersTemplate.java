package net.smileycorp.deeperdepths.common.world.chambers;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialPot;
import net.smileycorp.deeperdepths.common.world.base.DDStructureTemplate;
import net.smileycorp.deeperdepths.common.world.base.ModRand;
import net.smileycorp.deeperdepths.common.world.base.WorldGenCustomStructure;
import net.smileycorp.deeperdepths.common.world.chambers.spawners.WorldGenTrialSpawnerType;

import java.util.Random;

public class TrialChambersTemplate extends DDStructureTemplate {

    //gaurentees a random melee spawn
    protected WorldGenCustomStructure[] randomMeleeSpawner = {new WorldGenTrialSpawnerType("spawner_zombie"), new WorldGenTrialSpawnerType("spawner_husk"),
    new WorldGenTrialSpawnerType("spawner_spider")};

    //gaurentees a small melee spawn
    protected WorldGenCustomStructure[] randomSmallMeleeSpawner = {new WorldGenTrialSpawnerType("spawner_baby_zombie"), new WorldGenTrialSpawnerType("spawner_silver_fish"), new
            WorldGenTrialSpawnerType("spawner_slime"), new WorldGenTrialSpawnerType("spawner_cave_spider")};
    //gaurentees a random ranged spawn
    protected WorldGenCustomStructure[] randomRangedSpawner = {new WorldGenTrialSpawnerType("spawner_skeleton"), new WorldGenTrialSpawnerType("spawner_stray"),
    new WorldGenTrialSpawnerType("spawner_bogged")};

    //This one has a chance of spawning and is not gaurenteed
    protected WorldGenCustomStructure[] randomSpawnerOverall = {new WorldGenTrialSpawnerType("spawner_skeleton"),
            new WorldGenTrialSpawnerType("spawner_bogged"),new WorldGenTrialSpawnerType("spawner_zombie"), new WorldGenTrialSpawnerType("spawner_silver_fish"),
            new WorldGenTrialSpawnerType("spawner_spider"), new WorldGenTrialSpawnerType("spawner_husk"), new WorldGenTrialSpawnerType("spawner_slime") };

    private static final ResourceLocation LOOT = new ResourceLocation(Constants.MODID, "chamber_chest_loot");

    private static final ResourceLocation LOOT_POT = new ResourceLocation(Constants.MODID, "chamber_pot_loot");

    public TrialChambersTemplate() {

    }

    public TrialChambersTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);

    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //handles all functions and mini-structures

        if(function.startsWith("chest")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn() && sbb.isVecInside(blockPos)) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        } else if (function.startsWith("pot")) {
            BlockPos blockPos = pos.down();
            if(sbb.isVecInside(blockPos) && generatePotSpawn()) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState( pos, Blocks.AIR.getDefaultState(), 3);
                if(tileEntity instanceof TileTrialPot) {
                    TileTrialPot pot = (TileTrialPot) tileEntity;
                    //LOOT
                    pot.setLootTable(LOOT_POT, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }

        //Random Spawner by chance
        if(function.startsWith("r_spawner")) {
            if(generateRandomSpawner()) {
                world.setBlockToAir(pos);
                WorldGenCustomStructure piece = ModRand.choice(randomSpawnerOverall);
                piece.generate(world, rand, pos);
            } else  {
                world.setBlockToAir(pos);
            }
            //Random Melee Spawner
        } else if (function.startsWith("r_melee")) {
            world.setBlockToAir(pos);
            WorldGenCustomStructure piece = ModRand.choice(randomMeleeSpawner);
            piece.generate(world, rand, pos);
            //Random Small Melee Spawner
        } else if (function.startsWith("rs_melee")) {
            world.setBlockToAir(pos);
            WorldGenCustomStructure piece = ModRand.choice(randomSmallMeleeSpawner);
            piece.generate(world, rand, pos);
            //Random Ranged Spawner
        } else if (function.startsWith("r_range")) {
            world.setBlockToAir(pos);
            WorldGenCustomStructure piece = ModRand.choice(randomRangedSpawner);
            piece.generate(world, rand, pos);
            //Breeze Spawner
        } else if (function.startsWith("breeze")) {
            world.setBlockToAir(pos);
            WorldGenCustomStructure breeze_spawner = new WorldGenTrialSpawnerType("spawner_breeze");
            breeze_spawner.generate(world, rand, pos);
        }


        //Regular Vaults
        if(function.startsWith("vault")) {
            if(!generateVaultSpawn()) {
                world.setBlockToAir(pos.down());
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.add(1,0,0));
                world.setBlockToAir(pos.add(-1, 0, 0));
                world.setBlockToAir(pos.add(0, 0, 1));
                world.setBlockToAir(pos.add(0, 0, -1));
                world.setBlockToAir(pos.add(1, 0, 1));
                world.setBlockToAir(pos.add(1, 0, -1));
                world.setBlockToAir(pos.add(-1, 0, 1));
                world.setBlockToAir(pos.add(-1, 0, -1));
                //lower
                world.setBlockToAir(pos.add(1,-1,0));
                world.setBlockToAir(pos.add(-1, -1, 0));
                world.setBlockToAir(pos.add(0, -1, 1));
                world.setBlockToAir(pos.add(0, -1, -1));
                world.setBlockToAir(pos.add(1, -1, 1));
                world.setBlockToAir(pos.add(1, -1, -1));
                world.setBlockToAir(pos.add(-1, -1, 1));
                world.setBlockToAir(pos.add(-1, -1, -1));
            } else {
                world.setBlockToAir(pos);
            }
        }

        //Omnious Vaults
         else if(function.startsWith("omni_vault")) {
            if(!generateOminousVaultSpawn()) {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            } else {
                world.setBlockToAir(pos);
            }
        }



    }


    private boolean generateRandomSpawner() {
        int randomNumberSpawnerGenerator = ModRand.range(0, 8);
        return randomNumberSpawnerGenerator <= 5;
    }
    private boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        return randomNumberChestGenerator < 3;
    }

    private boolean generateVaultSpawn() {
        int randomNumberVaultGenerator = ModRand.range(0, 11);
        return randomNumberVaultGenerator < 6;
    }

    private boolean generateOminousVaultSpawn() {
        int randomOmniGenerator = ModRand.range(0, 11);
        return randomOmniGenerator < 8;
    }

    private boolean generatePotSpawn() {
        int randomPotGenerator = ModRand.range(0, 10);
        return randomPotGenerator < 6;
    }


    @Override
    public String templateLocation() {
        return "trial_chambers";
    }
}
