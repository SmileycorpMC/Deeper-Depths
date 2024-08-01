package net.smileycorp.deeperdepths.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.items.ItemMace;

public class EnchantmentBreach extends Enchantment
{
    public EnchantmentBreach()
    {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        setRegistryName(Constants.loc("breach"));
        setName(Constants.name("breach"));
    }

    @Override
    public int getMinEnchantability(int level)
    { return 15 + (level - 1) * 9; }

    @Override
    public int getMaxEnchantability(int level)
    { return 65 + (level - 1) * 9; }

    @Override
    public int getMaxLevel()
    { return 5; }

    @Override
    public boolean canApplyTogether(Enchantment enchantment)
    { return super.canApplyTogether(enchantment) && (enchantment != DeeperDepthsEnchantments.DENSITY && enchantment != Enchantments.SMITE && enchantment != Enchantments.BANE_OF_ARTHROPODS); }

    /** Forces this to ONLY be placeable on the Mace. */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem() instanceof ItemMace;
    }
}