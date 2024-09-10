package net.smileycorp.deeperdepths.common.integration;

import net.minecraft.item.ItemStack;
import net.smileycorp.deeperdepths.common.blocks.BlockDDStone;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumStoneType;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingRegistry;

public class ChiselIntegration {

    public static void registerRecipes() {
        ICarvingRegistry registry = CarvingUtils.getChiselRegistry();
        registerStoneVariants(registry, "stoneTuff", EnumStoneType.Material.TUFF, EnumStoneType.Material.TUFF_BRICKS);
        registerStoneVariants(registry, "stoneDeepslate", EnumStoneType.Material.DEEPSLATE, EnumStoneType.Material.DEEPSLATE_BRICKS,
                EnumStoneType.Material.DEEPSLATE_TILES);
    }

    private static void registerStoneVariants(ICarvingRegistry registry, String name, EnumStoneType.Material... materials) {
        CarvingUtils.addOreGroup(name);
        int i = 0;
        for (EnumStoneType.Material material : materials) for (EnumStoneType type : material.getTypes())
            registry.addVariation(name, CarvingUtils.variationFor(new ItemStack(DeeperDepthsBlocks.STONE, 1, type.ordinal()),
                    DeeperDepthsBlocks.STONE.getDefaultState().withProperty(BlockDDStone.VARIANT, type), i++));
    }

}
