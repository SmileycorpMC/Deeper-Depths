package com.deeperdepths.common.world.ancient_cities;

import com.deeperdepths.common.world.base.DDStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class AncientCitiesTemplate extends DDStructureTemplate {

    public AncientCitiesTemplate() {

    }

    public AncientCitiesTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);

    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //handles all functions and mini-structures

    }

    @Override
    public String templateLocation() {
        return "ancient_city";
    }
}
