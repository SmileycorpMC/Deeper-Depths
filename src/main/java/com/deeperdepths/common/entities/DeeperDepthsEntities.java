package com.deeperdepths.common.entities;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class DeeperDepthsEntities
{
    public static int id;

    public static void registerEntities()
    {
        registerEntity("bogged", EntityBogged.class, ++id, 80, 9084018, 3231003);
        registerEntity("breeze", EntityBreeze.class, ++id, 81, 12299217, 8355255);
        registerEntity("wind_charge", EntityWindCharge.class, ++id, 80);
        registerEntity("chambers_painting", EntityDDPainting.class, ++id, 81);
        registerEntity("ominous_item_spawner", EntityOminousItemSpawner.class, ++id, 8);
    }

    public static void registerEntitySpawns() {
        spawnRate(EntityBogged.class, EnumCreatureType.MONSTER, 5, 1, 3, BiomeDictionary.Type.SWAMP);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
    { EntityRegistry.registerModEntity(new ResourceLocation(Constants.MODID, name), entity, Constants.MODID + "." + name, id, DeeperDepths.instance, range, 1, true, color1, color2); }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range)
    { EntityRegistry.registerModEntity(new ResourceLocation(Constants.MODID, name), entity, Constants.MODID + "." + name, id, DeeperDepths.instance, range, 1, true); }

    private static void spawnRate(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, BiomeDictionary.Type biomesAllowed) {
        for(Biome biome: BiomeDictionary.getBiomes(biomesAllowed)) {
            if(biome != null && weight > 0) {
                EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

            }
        }
    }

}
