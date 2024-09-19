package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.entities.EntityDDPainting;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;

public class ItemCustomPainting extends ItemHangingEntity implements IMetaItem {

    public ItemCustomPainting() {
        super(EntityDDPainting.class);
        setUnlocalizedName(Constants.name("trial_chambers_painting"));
        setRegistryName(Constants.loc("trial_chambers_painting"));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos offsetPos = pos.offset(facing);

        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(offsetPos, facing, itemstack)) {
            EntityHanging entity = new EntityDDPainting(world, offsetPos, facing);

            if (entity != null && entity.onValidSurface()) {
                if (!world.isRemote) {
                    entity.playPlaceSound();
                    world.spawnEntity(entity);
                }

                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
