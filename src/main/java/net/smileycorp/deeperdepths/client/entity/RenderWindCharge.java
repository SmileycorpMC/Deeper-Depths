package net.smileycorp.deeperdepths.client.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.client.entity.model.ModelWindCharge;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.entities.EntityWindCharge;

@SideOnly(Side.CLIENT)
public class RenderWindCharge extends Render<EntityWindCharge>
{
    private final ModelWindCharge model = new ModelWindCharge();
    private static final ResourceLocation WIND_CHARGE_TEXTURE = new ResourceLocation(Constants.MODID + ":textures/entities/wind_charge.png");

    public RenderWindCharge(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(EntityWindCharge entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        boolean isInvisible = entity.isInvisible();
        GlStateManager.depthMask(!isInvisible);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.translate(0.0F, -1.501F, 0.0F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.bindEntityTexture(entity);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float ticksExisted = (float)entity.ticksExisted + partialTicks;
        GlStateManager.translate(ticksExisted * -0.02F, 0.0F, 0.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        this.model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, entityYaw, 0, 0.0625F);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(!isInvisible);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityWindCharge entityWindCharge)
    { return WIND_CHARGE_TEXTURE; }
}