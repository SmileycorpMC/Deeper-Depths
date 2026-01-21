package com.deeperdepths.common.world.chambers;

import com.deeperdepths.config.WorldConfig;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class WorldGenTrialChambers extends WorldGenerator {

    private int spacing;

    private int separation;
    public WorldGenTrialChambers() {

        this.spacing = WorldConfig.trial_chambers.getSpawnChances();
        this.separation = 16;
    }



    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        /**
         * MAKE THIS CONFIGURABLE, SPACING IS THE AMOUNT OF EACH TIME IT IS CAPAPABLE OF GENERATING
         */
        //old 525
        if(WorldConfig.trial_chambers.getSpawnChances() == 0) {
            return false;
        }

        if(canSpawnStructureAtPos(world, pos.getX() >> 4, pos.getZ() >> 4)) {
                getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random)
                        .generateStructure(world, random, new StructureBoundingBox(pos.getX() - 150, pos.getZ() - 150, pos.getX() + 150, pos.getZ() + 150));
                return true;
        }
        return false;
    }

    protected boolean canSpawnStructureAtPos(World world, int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.spacing - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.spacing - 1;
        }

        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        Random random =  world.setRandomSeed(k, l, 19930381);
        k = k * this.spacing;
        l = l * this.spacing;
        k = k + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (i == k && j == l)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }

    }

    public static boolean isAbleToSpawnHere(BlockPos pos, World world) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, BiomeDictionary.Type.OCEAN)) {
                return false;
            }
        return true;
    }


    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenTrialChambers.Start(world, rand , chunkX, chunkZ);
    }


    public static class Start extends StructureStart {

        public Start() {

        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.createChambers(worldIn, rand, chunkX, chunkZ);
        }


        protected void createChambers(World world, Random rand, int chunkX, int chunkZ) {
            Random random = new Random(chunkX + chunkZ * 10387313L);
            int rand2 = random.nextInt(Rotation.values().length);
            BlockPos posI = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);

            //You can add more additional parameters or second checks to things before it starts the physical structure
                for(int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand2 + i) % Rotation.values().length];
                    components.clear();
                    //Set IAW with the best Position to spawn the first layer of the chambers at
                    BlockPos blockpos = posI.add(0, WorldConfig.trial_chambers.getMinHeight(), 0);
                    TrialChambers chambers = new TrialChambers(world, world.getSaveHandler().getStructureTemplateManager(), components);
                    //Starts the first room within the Trial Chambers
                    chambers.startChambers(blockpos, rotation);
                    this.updateBoundingBox();



                    if (this.isSizeableStructure()) {

                        break;
                    }
                }
        }


        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
        {
            super.generateStructure(worldIn, rand, structurebb);
        }

        //Here is where you specify the size of the structure in terms of rooms, I guess you could make this configurable if you want
        @Override
        public boolean isSizeableStructure() {
            return components.size() > 6;
        }
    }

}
