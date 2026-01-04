package com.deeperdepths.common;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class EntityMaceDamageSource extends EntityDamageSource
{
    protected float percentage;

    public EntityMaceDamageSource(String damageTypeIn, Entity damageSourceEntityIn, float piercingPercentage)
    {
        super(damageTypeIn, damageSourceEntityIn);
        percentage = piercingPercentage;
    }

    public float getPercentage() { return percentage; }
}
