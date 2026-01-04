package com.deeperdepths.common;

import com.deeperdepths.client.particle.*;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;

public class Constants {
	
	public static final String NAME = "Deeper Depths";
	public static final String MODID = "deeperdepths";
	public static final String VERSION = "1.1.1";
	public static final String DEPENDENCIES = "required-after:atlaslib@[1.1.9,);after:fluidlogged_api@[3.1.0,)";
	public static final String PATH = "com.deeperdepths.";
	public static final String CLIENT = PATH + "client.ClientProxy";
	public static final String SERVER = PATH + "common.CommonProxy";
    public static final boolean FUNNY = isFunny();

	/** The damage source used by Maces. */
	public static DamageSource causeMaceDamage(Entity source, float breachPercent)
	{ return (new EntityMaceDamageSource(MODID + "." + "mace", source, breachPercent)); }

	public static String name(String name) {
		return MODID + "." + name.toLowerCase(Locale.US);
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
	
	private static boolean isFunny() {
		LocalDateTime time = LocalDateTime.now();
		return time.getDayOfMonth() == 1 && time.getMonth().equals(Month.APRIL);
	}

	/**
	 * This is used by the Particle Spawning as an ID system for out Particles.
	 * We do not require Ids for Particles, it's just more convenient for sending over packets!
	 * */
	@SideOnly(Side.CLIENT)
	public static IParticleFactory getFactory(int particleId)
	{
		switch(particleId)
		{
			default:
			case 0:
				return new ParticleTrialOmen.Factory();
			case 1:
				return new ParticleOmenRelease.Factory();
			case 2:
				return new ParticleOozingDrip.Factory();
			case 3:
				return new ParticleFlameScalable.Factory();
			case 4:
				return new ParticleSparkColorable.Factory();
			case 5:
				return new ParticleSpawnerDetect.Factory();
			case 6:
				return new ParticleGust.Factory();
			case 7:
				return new ParticleChargedGust.Factory();
		}
	}
}
