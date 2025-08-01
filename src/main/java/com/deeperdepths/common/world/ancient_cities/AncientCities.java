package com.deeperdepths.common.world.ancient_cities;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class AncientCities {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    public AncientCities(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }
}
