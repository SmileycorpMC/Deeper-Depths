package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.deeperdepths.common.Constants;

import java.util.Locale;

public class PotionDeeperDepths extends Potion
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/potion_effects.png");

    protected PotionDeeperDepths(String name, boolean isBad, int color, int icon)
    {
        super(isBad, color);
        setPotionName("effect.deeperdepths." + name);
        setRegistryName(Constants.loc(name.toLowerCase(Locale.US)));
        this.setIconIndex(icon % 8, icon / 8);
    }

    /* Required so the effect actually runs. */
    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public int getStatusIconIndex()
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        return super.getStatusIconIndex();
    }

    /** Generic timer for spawning particles. */
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
        if (entity.world.getTotalWorldTime() % getParticleSpawnRate(entity) == 0L && !entity.world.isRemote)
        { spawnParticles(entity); }
    }

    /** How often particles are spawned via. */
    public long getParticleSpawnRate(EntityLivingBase entity)
    { return 25L - entity.getRNG().nextInt(10); }

    /** The actual method to spawn particles. Make sure to use `((WorldServer)entity.world).spawnParticle` when setting up! */
    public void spawnParticles(EntityLivingBase entity)
    { }
}