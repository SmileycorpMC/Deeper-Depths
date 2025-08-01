package com.deeperdepths.common.advancements;

import com.deeperdepths.common.Constants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class DeeperDepthsCriterionTrigger implements ICriterionTrigger {
    
    private final ResourceLocation id;
    private final Map<PlayerAdvancements, Set<Listener>> listeners = Maps.newHashMap();
    
    public DeeperDepthsCriterionTrigger(String name) {
        id = Constants.loc(name);
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public void addListener(PlayerAdvancements advancements, Listener listener) {
        Set<Listener> set = listeners.get(advancements);
        if (set == null) {
            set = Sets.newHashSet();
            listeners.put(advancements, set);
        }
        set.add(listener);
    }
    
    @Override
    public void removeListener(PlayerAdvancements advancements, Listener listener) {
        Set<Listener> set = listeners.get(advancements);
        if (set != null) set.remove(listener);
    }
    
    @Override
    public void removeAllListeners(PlayerAdvancements advancements) {
        Set<Listener> set = listeners.get(advancements);
        if (set != null) set.clear();
    }
    
    @Override
    public ICriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new AbstractCriterionInstance(getId());
    }
    
    public void trigger(EntityPlayerMP player) {
        if (player == null) return;
        PlayerAdvancements advancements = player.getAdvancements();
        Set<Listener> set = listeners.get(advancements);
        if (set == null) return;
        for (Listener listener : set) listener.grantCriterion(advancements);
    }
    
}
