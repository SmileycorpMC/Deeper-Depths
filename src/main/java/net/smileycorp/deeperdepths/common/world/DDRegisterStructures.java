package net.smileycorp.deeperdepths.common.world;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.smileycorp.deeperdepths.common.world.chambers.TrialChambersTemplate;
import net.smileycorp.deeperdepths.common.world.chambers.WorldGenTrialChambers;

public class DDRegisterStructures {

    //this just registers structures with the system
    public static void handleStructureRegistries() {
        MapGenStructureIO.registerStructure(WorldGenTrialChambers.Start.class, "TrialChambers");
        MapGenStructureIO.registerStructureComponent(TrialChambersTemplate.class, "TCP");
    }
}
