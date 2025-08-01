package com.deeperdepths.config;

import java.util.function.Supplier;

public class ConfigurableHolder<T> {
    
    private final Supplier<T> supplier;
    private final Supplier<Boolean> shouldEnable;
    private T object;
    
    public ConfigurableHolder(Supplier<T> supplier, Supplier<Boolean> shouldEnable) {
        this.supplier = supplier;
        this.shouldEnable = shouldEnable;
    }
    
    public boolean isEnabled() {
        return shouldEnable.get();
    }
    
    public T get() {
        this.object = supplier.get();
        return object;
    }
    
}
