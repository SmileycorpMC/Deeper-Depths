package com.deeperdepths.common.advancements;

import com.deeperdepths.common.Constants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class SpyglassCriterionTrigger implements ICriterionTrigger<SpyglassCriterionTrigger.Instance> {

    private static final ResourceLocation ID = Constants.loc("spyglass");
    private final Map<PlayerAdvancements, Set<Listener<Instance>>> listeners = Maps.newHashMap();

    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public void addListener(PlayerAdvancements advancements, Listener listener) {
        Set<Listener<Instance>> set = listeners.computeIfAbsent(advancements, k -> Sets.newHashSet());
        set.add(listener);
    }
    
    @Override
    public void removeListener(PlayerAdvancements advancements, Listener listener) {
        Set<Listener<Instance>> set = listeners.get(advancements);
        if (set != null) set.remove(listener);
    }
    
    @Override
    public void removeAllListeners(PlayerAdvancements advancements) {
        Set<Listener<Instance>> set = listeners.get(advancements);
        if (set != null) set.clear();
    }
    
    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new Instance(EntityPredicate.deserialize(json.get("entity")));
    }

    public void trigger(EntityPlayerMP player, Entity entity) {
        if (player == null) return;
        PlayerAdvancements advancements = player.getAdvancements();
        Set<Listener<Instance>> set = listeners.get(advancements);
        Set<Listener<Instance>> passed = Sets.newHashSet();
        if (set == null) return;
        for (Listener<Instance> listener : set) if (listener.getCriterionInstance()
                .predicate.test(player, entity)) passed.add(listener);
        passed.forEach(listener -> listener.grantCriterion(advancements));
    }

    public static class Instance implements ICriterionInstance {

        private final EntityPredicate predicate;

        private Instance(EntityPredicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public ResourceLocation getId() {
            return SpyglassCriterionTrigger.ID;
        }

    }
    
}
