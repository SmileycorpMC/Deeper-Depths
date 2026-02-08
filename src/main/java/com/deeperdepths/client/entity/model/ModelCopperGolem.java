package com.deeperdepths.client.entity.model;

import com.deeperdepths.common.entities.EntityCopperGolem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCopperGolem extends ModelBase
{
    public final ModelRenderer main;
    public final ModelRenderer upper_body;
    private final ModelRenderer head;
    public final ModelRenderer arms;
    private final ModelRenderer armL;
    private final ModelRenderer armR;
    private final ModelRenderer legL;
    private final ModelRenderer legR;

    public ModelCopperGolem()
    {
        textureWidth = 64;
        textureHeight = 64;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);


        upper_body = new ModelRenderer(this);
        upper_body.setRotationPoint(0.0F, -5.0F, 0.0F);
        main.addChild(upper_body);
        upper_body.cubeList.add(new ModelBox(upper_body, 36, 0, -4.0F, -6.0F, -3.0F, 8, 6, 6, 0.0F, false));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -6.0F, 0.0F);
        upper_body.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -5.0F, -4.0F, 8, 5, 8, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 0, 32, -4.0F, -5.0F, -5.0F, 8, 5, 10, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 26, 0, -1.0F, -2.0F, -6.0F, 2, 3, 2, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 16, 17, -1.0F, -9.0F, -1.0F, 2, 4, 2, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 0, 15, -2.0F, -13.0F, -2.0F, 4, 4, 4, 0.0F, false));

        arms = new ModelRenderer(this);
        arms.setRotationPoint(0.0F, -6.0F, 0.0F);
        upper_body.addChild(arms);


        armL = new ModelRenderer(this);
        armL.setRotationPoint(4.0F, 0.0F, 0.0F);
        arms.addChild(armL);
        armL.cubeList.add(new ModelBox(armL, 50, 18, 0.0F, -1.0F, -2.0F, 3, 10, 4, 0.0F, false));

        armR = new ModelRenderer(this);
        armR.setRotationPoint(-4.0F, 0.0F, 0.0F);
        arms.addChild(armR);
        armR.cubeList.add(new ModelBox(armR, 36, 18, -3.0F, -1.0F, -2.0F, 3, 10, 4, 0.0F, true));

        legL = new ModelRenderer(this);
        legL.setRotationPoint(2.0F, -5.0F, 0.0F);
        main.addChild(legL);
        legL.cubeList.add(new ModelBox(legL, 16, 23, -2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F, false));

        legR = new ModelRenderer(this);
        legR.setRotationPoint(-2.0F, -5.0F, 0.0F);
        main.addChild(legR);
        legR.cubeList.add(new ModelBox(legR, 0, 23, -2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F, true));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    { main.render(scale); }


    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityCopperGolem golem = (EntityCopperGolem) entityIn;

        float f = 1.0F;

        if (false)
        {
            f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1.0F)
        { f = 1.0F; }


        this.arms.rotateAngleX = 0F;
        this.armL.rotateAngleX = 0F;
        this.armR.rotateAngleX = 0F;
        this.armR.rotateAngleZ = 0F;
        this.armL.rotateAngleZ = 0F;

        this.legR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.legL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;

        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;

        if (!golem.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
        {
            this.arms.rotateAngleX = -1F;
            this.armL.rotateAngleX = 0F;
            this.armR.rotateAngleX = this.armL.rotateAngleX;
            this.armR.rotateAngleZ = -0.4F;
            this.armL.rotateAngleZ = -this.armR.rotateAngleZ;
        }
    }
}