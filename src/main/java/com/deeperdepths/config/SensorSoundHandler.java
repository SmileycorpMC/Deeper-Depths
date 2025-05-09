package com.deeperdepths.config;

import com.deeperdepths.common.ISoundFrequency;
import com.google.common.collect.Maps;
import net.minecraft.util.SoundEvent;

import java.util.Map;

public class SensorSoundHandler {
    
    private static final Map<String, Integer> SOUND_STRENGTHS = Maps.newLinkedHashMap();
    
    public static void init() {
        SOUND_STRENGTHS.put("block.cloth", 0);
        SOUND_STRENGTHS.put("sculk_sensor.clicking", 0);
        SOUND_STRENGTHS.put("step", 1);
        SOUND_STRENGTHS.put("fall", 1);
        SOUND_STRENGTHS.put("swim", 1);
        SOUND_STRENGTHS.put("flap", 1);
        SOUND_STRENGTHS.put("entity.arrow.hit", 2);
        SOUND_STRENGTHS.put("hit", 0);
        SOUND_STRENGTHS.put("splash", 2);
        SOUND_STRENGTHS.put("shoot", 3);
        SOUND_STRENGTHS.put("equip", 5);
        SOUND_STRENGTHS.put("shake", 4);
        SOUND_STRENGTHS.put("roar", 4);
        SOUND_STRENGTHS.put("flying", 4);
        SOUND_STRENGTHS.put("fill", 12);
        SOUND_STRENGTHS.put("empty", 13);
        SOUND_STRENGTHS.put("item", 3);
        SOUND_STRENGTHS.put("armor", 5);
        SOUND_STRENGTHS.put("saddle", 5);
        SOUND_STRENGTHS.put("shear", 6);
        SOUND_STRENGTHS.put("hurt", 7);
        SOUND_STRENGTHS.put(".eat", 8);
        SOUND_STRENGTHS.put("drink", 8);
        SOUND_STRENGTHS.put("close", 9);
        SOUND_STRENGTHS.put("click_off", 9);
        SOUND_STRENGTHS.put("detach", 9);
        SOUND_STRENGTHS.put("click_on", 10);
        SOUND_STRENGTHS.put("attach", 10);
        SOUND_STRENGTHS.put("lever", 10);
        SOUND_STRENGTHS.put("primed", 10);
        SOUND_STRENGTHS.put("note", 10);
        SOUND_STRENGTHS.put("break", 12);
        SOUND_STRENGTHS.put("place", 13);
        SOUND_STRENGTHS.put("block", 11);
        SOUND_STRENGTHS.put("cast_spell", 14);
        SOUND_STRENGTHS.put("teleport", 14);
        SOUND_STRENGTHS.put("death", 14);
        SOUND_STRENGTHS.put("explode", 14);
    }
    
    public static int getSoundFrequency(SoundEvent sound) {
        if (((ISoundFrequency)sound).isInitialized()) return ((ISoundFrequency) sound).getSoundFrequency();
        String name = sound.getRegistryName().getResourcePath();
        for (Map.Entry<String, Integer> entry : SOUND_STRENGTHS.entrySet()) if (name.contains(entry.getKey()))
            return ((ISoundFrequency) sound).setSoundFrequency(entry.getValue());
        return ((ISoundFrequency) sound).setSoundFrequency(0);
    }
    
}
