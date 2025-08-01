package com.deeperdepths.common.items;

import com.deeperdepths.common.blocks.BlockTrial;
import com.deeperdepths.common.blocks.tiles.TileTrial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemConfigurator extends ItemDeeperDepths {
    
    public ItemConfigurator() {
        super("configurator");
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (player.isCreative() && player.canPlayerEdit(pos, facing, player.getHeldItem(hand))
                && state.getBlock() instanceof BlockTrial && world.getTileEntity(pos) instanceof TileTrial) {
            state.getBlock().
        }
        return EnumActionResult.PASS;
    }
    
    @Override
    public boolean canItemEditBlocks() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
       tooltip.add(I18n.format("item.deeperdepths.configurator.tooltip"));
    }
    
    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
    
}
