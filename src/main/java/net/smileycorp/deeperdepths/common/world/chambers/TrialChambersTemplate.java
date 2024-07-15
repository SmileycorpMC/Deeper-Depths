package net.smileycorp.deeperdepths.common.world.chambers;

import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.smileycorp.deeperdepths.common.world.base.DDStructureTemplate;

import java.util.Random;

public class TrialChambersTemplate extends DDStructureTemplate {


    public TrialChambersTemplate() {

    }

    public TrialChambersTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);

    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //handles all functions and mini-structures
    }





    @Override
    public String templateLocation() {
        return "trial_chambers";
    }
}
