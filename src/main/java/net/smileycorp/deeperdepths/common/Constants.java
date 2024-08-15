package net.smileycorp.deeperdepths.common;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;

import java.time.LocalDateTime;
import java.time.Month;

public class Constants {
	
	public static final String NAME = "Deeper Depths";
	public static final String MODID = "deeperdepths";
	public static final String VERSION = "0.1.1";
	public static final String DEPENDENCIES = "required-after:atlaslib";
	public static final String PATH = "net.smileycorp.deeperdepths.";
	public static final String CLIENT = PATH + "client.ClientProxy";
	public static final String SERVER = PATH + "common.CommonProxy";
    public static final boolean IS_COPPER_TASTY = isCopperTasty();

	/** The damage source used by Maces. */
	public static DamageSource causeMaceDamage(Entity source)
	{ return (new EntityDamageSource(MODID + "." + "mace", source)); }

	public static String name(String name) {
		return MODID + "." + name.replace("_", "").replace(" ", "");
	}
	
	public static ResourceLocation loc(String name) {
		return new ResourceLocation(MODID, name.toLowerCase());
	}
	
	public static String locStr(String name) {
		return loc(name).toString();
	}
	
	public static NBTTagCompound getEntityTag(ResourceLocation id) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("id", id.toString());
		return tag;
	}
	
	private static boolean isCopperTasty() {
		LocalDateTime time = LocalDateTime.now();
		return time.getDayOfMonth() == 1 && time.getMonth().equals(Month.APRIL);
	}
	
}
