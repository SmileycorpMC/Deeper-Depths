package com.deeperdepths.common.integration;

import com.deeperdepths.common.blocks.BlockDDStone;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingRegistry;

public class ChiselIntegration {

    public static void registerRecipes() {
        ICarvingRegistry registry = CarvingUtils.getChiselRegistry();
        registerStoneVariants(registry, "stoneTuff", EnumStoneType.Material.TUFF, EnumStoneType.Material.TUFF_BRICKS);
        registerStoneVariants(registry, "stoneDeepslate", EnumStoneType.Material.DEEPSLATE, EnumStoneType.Material.DEEPSLATE_BRICKS,
                EnumStoneType.Material.DEEPSLATE_TILES);
    }

    private static void registerStoneVariants(ICarvingRegistry registry, String name, EnumStoneType.Material... materials) {
        ICarvingGroup group = CarvingUtils.getDefaultGroupFor(name);
        registry.addGroup(group);
        int i = 0;
        for (EnumStoneType.Material material : materials) for (EnumStoneType type : material.getTypes())
            registry.addVariation(name, CarvingUtils.variationFor(new ItemStack(DeeperDepthsBlocks.STONE, 1, type.ordinal()),
                    DeeperDepthsBlocks.STONE.getDefaultState().withProperty(BlockDDStone.VARIANT, type), i++));
    }

}
