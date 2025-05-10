package com.deeperdepths.common.capabilities;

import com.deeperdepths.config.SensorSoundHandler;
import com.google.common.collect.Sets;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.WorldServer;

import java.util.HashSet;

public class VibrationHandler {
    
    private static VibrationHandler instance;
    
    private final WorldServer world;
    private final HashSet<Listener> listeners = Sets.newHashSet();
    
    public VibrationHandler(WorldServer world) {
        this.world = world;
    }
    
    public void add(Listener listener) {
        listeners.add(listener);
    }
    
    public void remove(Listener listener) {
        listeners.remove(listener);
    }
    
    public void playSound(SoundEvent sound, double x, double y, double z) {
        if (SensorSoundHandler.getSoundFrequency(sound) == 0) return;
        for (Listener listener : listeners) if (listener.canHear(sound, x, y, z)) listener.reactToSound(sound, x, y, z);
    }
    
    public static VibrationHandler getInstance(WorldServer world) {
       if (instance == null) instance = new VibrationHandler(world);
       else if (instance.world != world) instance = new VibrationHandler(world);
       return instance;
    }
    
    public static class Vibration {
    
    }
    
    public interface Listener {
        
        boolean canHear(SoundEvent sound, double x, double y, double z);
        
        void reactToSound(SoundEvent sound, double x, double y, double z);
        
    }
}
