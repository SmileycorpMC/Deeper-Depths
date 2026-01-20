package com.deeperdepths.common;

import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class DeeperDepthsTab extends CreativeTabs {
    
    private ItemStack stack;
    private boolean needsRefresh = true;
    
    public DeeperDepthsTab() {
        super(Constants.name("tab"));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (!needsRefresh && Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 1) needsRefresh = true;
        //change the item every 80 ticks
        if (stack == null || (needsRefresh && Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 0)) {
            stack = new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stack == null ? 0 : (stack.getMetadata() + 1) % 4);
            needsRefresh = false;
        }
        return stack;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return getIconItemStack();
    }
    
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> stacks) {
        super.displayAllRelevantItems(stacks);
        List<ItemStack> cached = Lists.newArrayList(stacks);
        stacks.clear();
        stacks.add(new ItemStack(DeeperDepthsBlocks.DEEPSLATE));
        stacks.add(new ItemStack(DeeperDepthsBlocks.DEEPSLATE, 1, 1));
        for (EnumStoneType.Material material : EnumStoneType.Material.values()) {
            if (material == EnumStoneType.Material.CALCITE) continue;
            for (EnumStoneType type : material.getTypes()) {
                stacks.add(type.getStack());
                if (type.hasVariants()) {
                    stacks.add(new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(type)));
                    stacks.add(new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, type.getShapedMeta()));
                    stacks.add(new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, type.getShapedMeta()));
                }
            }
            if (material == EnumStoneType.Material.DEEPSLATE_TILES) stacks.add(new ItemStack(DeeperDepthsBlocks.REINFORCED_DEEPSLATE));
        }
        for (EnumWeatherStage stage : EnumWeatherStage.values()) {
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_DOORS.get(stage).getItem()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_TRAPDOORS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_BULB, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.LIGHTNING_RODS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_CHEST, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_BARS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_CHAINS, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_LANTERN, 1, stage.ordinal()));
            //stacks.add(new ItemStack(DeeperDepthsBlocks.GOLEM_STATUE, 1, stage.ordinal()));
        }
        for (EnumWeatherStage stage : EnumWeatherStage.values()) {
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal() + 4));
            stacks.add(new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal() + 4));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 1, stage.ordinal() + 4));
            stacks.add(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal() + 4));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_CUT_COPPER_STAIRS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 1, stage.ordinal() + 4));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_DOORS.get(stage).getItem()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_TRAPDOORS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_BULB, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_LIGHTNING_RODS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_CHEST, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_BARS.get(stage)));
            stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_CHAINS, 1, stage.ordinal()));
            stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_LANTERN, 1, stage.ordinal() + 4));
            //stacks.add(new ItemStack(DeeperDepthsBlocks.WAXED_GOLEM_STATUE, 1, stage.ordinal()));
        }
        stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_TORCH));
        stacks.add(new ItemStack(DeeperDepthsBlocks.COPPER_ORE));
        for (ItemStack stack : cached) if (!contains(stack, stacks)) stacks.add(stack);
    }

    private ItemStack monsterEgg(ResourceLocation loc) {
        ItemStack stack = new ItemStack(Items.SPAWN_EGG);
        ItemMonsterPlacer.applyEntityIdToItemStack(stack, loc);
        return stack;
    }

    private boolean contains(ItemStack stack, List<ItemStack> stacks) {
        for (ItemStack stack1 : stacks) if (stack1.isItemEqual(stack)) return true;
        return false;
    }
    
    private void addItem(ItemStack stack, NonNullList<ItemStack> stacks, NonNullList<ItemStack> newStacks) {
        newStacks.add(stack);
        stacks.remove(stack);
    }
    
}
