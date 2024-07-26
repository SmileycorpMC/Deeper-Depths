package net.smileycorp.deeperdepths.client.entity.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBoggedMushrooms extends ModelSkeleton
{
    private final ModelRenderer mushroom3_r1;
    private final ModelRenderer mushroom2b_r1;
    private final ModelRenderer mushroom2a_r1;
    private final ModelRenderer mushroom1b_r1;
    private final ModelRenderer mushroom1a_r1;

    public ModelBoggedMushrooms()
    {
        textureWidth = 64;
        textureHeight = 32;

        mushroom3_r1 = new ModelRenderer(this);
        mushroom3_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addChild(mushroom3_r1);
        mushroom3_r1.cubeList.add(new ModelBox(mushroom3_r1, 21, 0, -6.0F, -2.0F, 2.0F, 4, 0, 4, 0.0F, false));
        mushroom3_r1.cubeList.add(new ModelBox(mushroom3_r1, -2, 5, -4.0F, -3.0F, 3.0F, 3, 0, 3, 0.0F, false));

        mushroom2b_r1 = new ModelRenderer(this);
        mushroom2b_r1.setRotationPoint(2.25F, -7.9F, 3.5F);
        this.bipedHead.addChild(mushroom2b_r1);
        setRotationAngle(mushroom2b_r1, 0.0F, 0.7854F, 0.0F);
        mushroom2b_r1.cubeList.add(new ModelBox(mushroom2b_r1, 25, -1, 0.0F, -3.1F, -3.0F, 0, 3, 6, 0.0F, false));

        mushroom2a_r1 = new ModelRenderer(this);
        mushroom2a_r1.setRotationPoint(2.25F, -7.9F, 3.5F);
        this.bipedHead.addChild(mushroom2a_r1);
        setRotationAngle(mushroom2a_r1, 0.0F, -0.7854F, 0.0F);
        mushroom2a_r1.cubeList.add(new ModelBox(mushroom2a_r1, 25, -1, 0.0F, -3.1F, -3.0F, 0, 3, 6, 0.0F, false));

        mushroom1b_r1 = new ModelRenderer(this);
        mushroom1b_r1.setRotationPoint(-3.0F, -7.9F, -3.0F);
        this.bipedHead.addChild(mushroom1b_r1);
        setRotationAngle(mushroom1b_r1, 0.0F, 0.7854F, 0.0F);
        mushroom1b_r1.cubeList.add(new ModelBox(mushroom1b_r1, 1, -3, 0.0F, -3.1F, -1.5F, 0, 3, 3, 0.0F, false));

        mushroom1a_r1 = new ModelRenderer(this);
        mushroom1a_r1.setRotationPoint(-3.0F, -7.9F, -3.0F);
        this.bipedHead.addChild(mushroom1a_r1);
        setRotationAngle(mushroom1a_r1, 0.0F, -0.7854F, 0.0F);
        mushroom1a_r1.cubeList.add(new ModelBox(mushroom1a_r1, 1, -3, 0.0F, -3.1F, -1.5F, 0, 3, 3, 0.0F, false));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    { super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn); }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}