package com.deeperdepths.integration.chisel;

import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CopperCarvingGroup implements ICarvingGroup {

    private List<ICarvingVariation> variations = null;

    @Override
    public String getName() {
        return "blockCopper";
    }

    @Nullable
    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public void setSound(@Nullable SoundEvent sound) {}

    @Nullable
    @Override
    public String getOreName() {
        return null;
    }

    @Override
    public void setOreName(@Nullable String oreName) {}

    @Override
    public List<ICarvingVariation> getVariations() {
        if (variations == null) {
            AtomicInteger i = new AtomicInteger();
            variations = OreDictionary.getOres("blockCopper").stream().filter(stack -> stack.getItem()
                            != Item.getItemFromBlock(DeeperDepthsBlocks.COPPER_BLOCK) || stack.getMetadata() == 0)
                    .map(stack -> CarvingUtils.variationFor(stack, i.getAndIncrement())).collect(Collectors.toList());
        }
        return Lists.newArrayList(variations);
    }

    @Override
    public void addVariation(ICarvingVariation variation) {}

    @Override
    public boolean removeVariation(ICarvingVariation variation) {
        return false;
    }

}
