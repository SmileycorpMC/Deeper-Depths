package net.smileycorp.deeperdepths.common;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;

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
    
}
