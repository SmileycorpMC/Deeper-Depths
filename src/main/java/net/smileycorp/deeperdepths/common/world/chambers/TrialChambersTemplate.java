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
import net.smileycorp.deeperdepths.common.world.base.DDStructureTemplate;
import net.smileycorp.deeperdepths.common.world.base.ModRand;

import java.util.Random;

public class TrialChambersTemplate extends DDStructureTemplate {


    private static final ResourceLocation LOOT = new ResourceLocation(Constants.MODID, "chamber_chest_loot");

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
        if(function.startsWith("omni_vault")) {
            if(!generateOminousVaultSpawn()) {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            } else {
                world.setBlockToAir(pos);
            }
        }

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


    @Override
    public String templateLocation() {
        return "trial_chambers";
    }
}
