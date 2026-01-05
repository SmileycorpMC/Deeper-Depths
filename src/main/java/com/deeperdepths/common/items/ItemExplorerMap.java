package com.deeperdepths.common.items;

import com.deeperdepths.common.commands.CommandLocateDD;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class ItemExplorerMap extends ItemDeeperDepths {

    public ItemExplorerMap() {
        super("explorer_map");
        setHasSubtypes(true);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos = player.getPosition();
        BlockPos structure = stack.getMetadata() == 1 ? CommandLocateDD.findNearestPosAncientCity(world, pos)
                : CommandLocateDD.findNearestPos(world, pos);
        if (structure == null) return super.onItemRightClick(world, player, hand);
        ItemStack map = ItemMap.setupNewMap(world, structure.getX(), structure.getZ(), (byte)2, true, true);
        ItemMap.renderBiomePreviewMap(world, map);
        MapData.addTargetDecoration(map, structure, "+", MapDecoration.Type.TARGET_X);
        map.setTranslatableName(getUnlocalizedName(stack) + ".name");
        stack.shrink(1);
        if (stack.isEmpty()) return new ActionResult<>(EnumActionResult.SUCCESS, map);
        if (!player.inventory.addItemStackToInventory(map.copy())) player.dropItem(map, false);
        player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item.deeperdepths." + byMeta(stack.getMetadata());
    }

    @Override
    public int getMaxMeta() {
        return 2;
    }

    @Override
    public String byMeta(int meta) {
        return (meta == 1 ? "city" : "trial") + "_explorer_map";
    }

}
