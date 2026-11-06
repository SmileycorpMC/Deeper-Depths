package com.deeperdepths.common.potion;

import com.deeperdepths.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class PotionDeeperDepths extends Potion
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/potion_effects.png");
    protected final int maxLevel;

    protected PotionDeeperDepths(String name, boolean isBad, int color, int icon, int maxLevel)
    {
        super(isBad, color);
        setPotionName("effect.deeperdepths." + name);
        setRegistryName(Constants.loc(name.toLowerCase(Locale.US)));
        this.setIconIndex(icon % 8, icon / 8);
        this.maxLevel = maxLevel;
    }

    protected PotionDeeperDepths(String name, boolean isBad, int color, int icon) {
        this(name, isBad, color, icon, -1);
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

    /** prevent rendering vanilla inventory text if the potion has a max level set */
    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return maxLevel == -1;
    }

    /**custom render the inventory text
     * fixes compat issues with certain mods (mainly one of the tweak mods that adds this feature for other effcts)
     * also it removes the need for a mixin
     * only used for bad omen and trial omen at the moment to match raids backport's rendering */
    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        if (shouldRenderInvText(effect)) return;
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        StringBuilder builder = new StringBuilder(I18n.format(getName()));
        builder.append(" ");
        if (effect.getAmplifier() > maxLevel) builder.append(isBadEffect() ? TextFormatting.RED : TextFormatting.GREEN);
        builder.append(I18n.format("enchantment.level." + (effect.getAmplifier() + 1)));
        fontRenderer.drawStringWithShadow(builder.toString(), (float)(x + 10 + 18), (float)(y + 6), 16777215);
        fontRenderer.drawStringWithShadow(Potion.getPotionDurationString(effect, 1), x + 10 + 18, y + 6 + 10, 8355711);
    }

}