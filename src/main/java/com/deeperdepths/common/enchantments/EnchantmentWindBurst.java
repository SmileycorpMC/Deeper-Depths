package com.deeperdepths.common.enchantments;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.items.ItemMace;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentWindBurst extends Enchantment
{
    public EnchantmentWindBurst()
    {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        setRegistryName(Constants.loc("wind_burst"));
        setName(Constants.name("wind_burst"));
    }

    @Override
    public int getMinEnchantability(int level)
    { return 15 + (level - 1) * 9; }

    @Override
    public int getMaxEnchantability(int level)
    { return 65 + (level - 1) * 9; }

    @Override
    public int getMaxLevel()
    { return 3; }

    /** Forces this to ONLY be placeable on the Mace. */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem() instanceof ItemMace;
    }
}