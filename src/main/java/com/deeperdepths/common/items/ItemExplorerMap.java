package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.commands.CommandLocateDD;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class ItemExplorerMap extends ItemDeeperDepths {

    public ItemExplorerMap() {
        super("explorer_map");
        setHasSubtypes(true);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos pos = player.getPosition();
        BlockPos structure = getStructure(world, pos, stack.getMetadata());
        if (structure == null) return super.onItemRightClick(world, player, hand);
        ItemStack map = ItemMap.setupNewMap(world, structure.getX(), structure.getZ(), (byte)2, true, true);
        ItemMap.renderBiomePreviewMap(world, map);
        setStructure(world, map, structure, Type.get(stack.getMetadata()));
        map.setTranslatableName(getUnlocalizedName(stack) + ".name");
        stack.shrink(1);
        if (stack.isEmpty()) return new ActionResult<>(EnumActionResult.SUCCESS, map);
        if (!player.inventory.addItemStackToInventory(map.copy())) player.dropItem(map, false);
        player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public static BlockPos getStructure(World world, BlockPos pos, int meta) {
        if (world == null || pos == null) return null;
        switch (meta) {
            case 0:
                return CommandLocateDD.findNearestTrialChamber(world, pos);
            case 2:
                return CommandLocateDD.findNearestAncientCity(world, pos);
            default:
                return null;
        }
    }

    public static void setStructure(World world, ItemStack stack, BlockPos pos, Type structure) {
        MapData data = Items.FILLED_MAP.getMapData(stack, world);
        if (data == null) return;
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("type", (byte) structure.ordinal());
        nbt.setInteger("x", pos.getX());
        nbt.setInteger("z", pos.getZ());
        //vanilla does not send this data to the client for some reason, so we gotta do it ourself
        nbt.setDouble("centerX", data.xCenter);
        nbt.setDouble("centerZ", data.zCenter);
        stack.setTagInfo("DDStructure", nbt);
    }

    //vanilla does not send this data to the client for some reason, so we gotta do it ourself
    public static void updateCenter(ItemStack stack, MapData data) {
        if (!stack.hasTagCompound()) return;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("DDStructure")) return;
        NBTTagCompound structure = nbt.getCompoundTag("DDStructure");
        structure.setDouble("centerX", data.xCenter);
        structure.setDouble("centerZ", data.zCenter);
    }

    public static Type getType(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("DDStructure")) return null;
        NBTTagCompound structure = nbt.getCompoundTag("DDStructure");
        return structure.hasKey("type") ? Type.get(structure.getByte("type")) : null;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < 4; i++) items.add(new ItemStack(this, 1, i));
    }


    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item.deeperdepths." + byMeta(stack.getMetadata());
    }

    @Override
    public int getMaxMeta() {
        return Type.values().length;
    }

    @Override
    public String byMeta(int meta) {
        return Type.getName(meta);
    }

    public enum Type {
        TRIAL("trial"),
        GEODE("geode"),
        CITY("city");

        private final String name;
        private final ResourceLocation texture;

        Type(String name) {
            this.name = name;
            this.texture = Constants.loc("textures/map/" + name + ".png");
        }

        public static String getName(int meta) {
            if (meta >= values().length) return "explorer_map";
            return values()[meta].name + "_explorer_map";
        }

        public static Type get(int meta) {
            return values()[meta];
        }

        public ResourceLocation getTexture() {
            return texture;
        }

    }

}
