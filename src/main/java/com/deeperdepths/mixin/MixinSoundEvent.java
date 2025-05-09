package com.deeperdepths.mixin;

import com.deeperdepths.common.ISoundFrequency;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SoundEvent.class)
public class MixinSoundEvent implements ISoundFrequency {
    
    private int frequency = -1;
    
    @Override
    public int setSoundFrequency(int frequency) {
        this.frequency = frequency;
        return frequency;
    }
    
    @Override
    public int getSoundFrequency() {
        return frequency;
    }
    
}
