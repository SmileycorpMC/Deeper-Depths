package com.deeperdepths.common.world;

import com.deeperdepths.common.world.chambers.TrialChambersTemplate;
import com.deeperdepths.common.world.chambers.WorldGenTrialChambers;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class DDRegisterStructures {

    //this just registers structures with the system
    public static void handleStructureRegistries() {
        MapGenStructureIO.registerStructure(WorldGenTrialChambers.Start.class, "TrialChambers");
        MapGenStructureIO.registerStructureComponent(TrialChambersTemplate.class, "TCP");
    }
}
