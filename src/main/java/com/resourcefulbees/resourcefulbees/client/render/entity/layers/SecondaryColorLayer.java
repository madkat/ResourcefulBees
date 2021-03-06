package com.resourcefulbees.resourcefulbees.client.render.entity.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.resourcefulbees.resourcefulbees.ResourcefulBees;
import com.resourcefulbees.resourcefulbees.api.beedata.ColorData;
import com.resourcefulbees.resourcefulbees.client.render.entity.models.CustomBeeModel;
import com.resourcefulbees.resourcefulbees.entity.passive.CustomBeeEntity;
import com.resourcefulbees.resourcefulbees.lib.BeeConstants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@Deprecated
@OnlyIn(Dist.CLIENT)
public class SecondaryColorLayer extends LayerRenderer<CustomBeeEntity, CustomBeeModel<CustomBeeEntity>> {

    private final ResourceLocation primaryLayerTexture;
    private final float[] secondaryColor;

    public SecondaryColorLayer(IEntityRenderer<CustomBeeEntity, CustomBeeModel<CustomBeeEntity>> rendererIn, ColorData colorData) {
        super(rendererIn);

        primaryLayerTexture = new ResourceLocation(ResourcefulBees.MOD_ID, BeeConstants.ENTITY_TEXTURES_DIR + colorData.getSecondaryLayerTexture() + ".png");
        secondaryColor = colorData.getSecondaryColorFloats();
    }

    public void render(@Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int packedLightIn, @Nonnull CustomBeeEntity customBeeEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderModel(this.getEntityModel(), primaryLayerTexture, matrixStackIn, bufferIn, packedLightIn, customBeeEntity, secondaryColor[0], secondaryColor[1], secondaryColor[2]);

        /*        CustomBeeData bee = customBeeEntity.getBeeData();

        if (bee.getColorData().isBeeColored() && bee.getColorData().hasSecondaryColor()) {
            float[] secondaryColor = bee.getColorData().getSecondaryColorFloats();
            ResourceLocation location = new ResourceLocation(ResourcefulBees.MOD_ID, BeeConstants.ENTITY_TEXTURES_DIR + bee.getColorData().getSecondaryLayerTexture() + ".png");
            renderModel(this.getEntityModel(), location, matrixStackIn, bufferIn, packedLightIn, customBeeEntity, secondaryColor[0], secondaryColor[1], secondaryColor[2]);
        }*/
    }
}
