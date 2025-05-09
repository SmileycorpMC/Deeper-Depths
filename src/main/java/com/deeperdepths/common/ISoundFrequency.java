package com.deeperdepths.common;

public interface ISoundFrequency {
    
    int setSoundFrequency(int frequency);
    
    int getSoundFrequency();
    
    default boolean isSensable() {
        return getSoundFrequency() > 0;
    }
    
    default boolean isInitialized() {
        return getSoundFrequency() >= 0;
    }
    
}
