package com.deeperdepths.common.capabilities;

import com.deeperdepths.common.blocks.tiles.TileSculkCatalyst;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.HashSet;
import java.util.Map;

public class SculkCatalysts {

    private static final Map<Integer, SculkCatalysts> INSTANCES = Maps.newHashMap();

    private final WorldServer world;
    private final HashSet<TileSculkCatalyst> catalysts = Sets.newHashSet();
    private Entity ctxEntity = null;

    public SculkCatalysts(WorldServer world) {
        this.world = world;
    }

    public void add(TileSculkCatalyst listener) {
        catalysts.add(listener);
    }
    
    public void remove(TileSculkCatalyst listener) {
        catalysts.remove(listener);
    }
    
    public boolean tryConsume(EntityLivingBase entity, int experience) {
        if (experience <= 0) return false;
        TileSculkCatalyst closest = null;
        double closestDistance = -1;
        for (TileSculkCatalyst catalyst : catalysts) {
            double distance = DirectionUtils.centerOf(catalyst.getPos()).squareDistanceTo(entity.posX, entity.posY, entity.posZ);
            if (distance > 64) continue;
            if (closestDistance != -1 && distance > closestDistance) continue;
            closest = catalyst;
            closestDistance = distance;
        }
        if (closest == null) return false;
        closest.bloom(entity, experience);
        return true;
    }
    
    public static SculkCatalysts getInstance(WorldServer world) {
        int dimension = world.provider.getDimension();
        if (INSTANCES.containsKey(dimension)) {
            SculkCatalysts instance = INSTANCES.get(dimension);
            if (instance.world == world) return instance;
            INSTANCES.clear();
        }
        SculkCatalysts instance = new SculkCatalysts(world);
        INSTANCES.put(dimension, instance);
        return instance;
    }

}
