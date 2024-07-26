package net.smileycorp.deeperdepths.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBreeze  extends ModelBase
{
    private final ModelRenderer head;
    private final ModelRenderer headlayer;
    private final ModelRenderer stick1;
    private final ModelRenderer stick2;
    private final ModelRenderer stick3;

    public ModelBreeze() {
        textureWidth = 64;
        textureHeight = 32;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false));

        headlayer = new ModelRenderer(this);
        headlayer.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(headlayer);
        headlayer.cubeList.add(new ModelBox(headlayer, 32, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.2F, false));

        stick1 = new ModelRenderer(this);
        stick1.setRotationPoint(0.0F, 7.0F, -5.0F);
        setRotationAngle(stick1, 0.3927F, 0.0F, 0.0F);
        stick1.cubeList.add(new ModelBox(stick1, 0, 16, -1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

        stick2 = new ModelRenderer(this);
        stick2.setRotationPoint(-4.0F, 7.0F, 4.0F);
        setRotationAngle(stick2, -2.7489F, 0.8727F, 3.1416F);
        stick2.cubeList.add(new ModelBox(stick2, 0, 16, -1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

        stick3 = new ModelRenderer(this);
        stick3.setRotationPoint(4.0F, 7.0F, 4.0F);
        setRotationAngle(stick3, -2.7489F, -0.6981F, 3.1416F);
        stick3.cubeList.add(new ModelBox(stick3, 0, 16, -1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        head.render(f5);
        stick1.render(f5);
        stick2.render(f5);
        stick3.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}