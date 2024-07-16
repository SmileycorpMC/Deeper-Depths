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


    }


    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= 2) {
            return false;
        }
        return true;
    }


    @Override
    public String templateLocation() {
        return "trial_chambers";
    }
}
