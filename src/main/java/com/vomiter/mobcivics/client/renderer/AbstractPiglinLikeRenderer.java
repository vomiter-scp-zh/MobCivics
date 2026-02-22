package com.vomiter.mobcivics.client.renderer;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPiglinLikeRenderer<
        T extends Mob,
        M extends PiglinModel<T>
        > extends HumanoidMobRenderer<T, M> {

    private final ResourceLocation baseTexture;

    protected AbstractPiglinLikeRenderer(
            EntityRendererProvider.Context ctx,
            M model,
            ModelLayerLocation innerArmorLayer,
            ModelLayerLocation outerArmorLayer,
            float shadowRadius,
            ResourceLocation baseTexture,
            boolean enableArmorLayer
    ) {
        super(ctx, model, shadowRadius,
                1.0019531F, 1.0F, 1.0019531F // 原版 PiglinRenderer head scale
        );
        this.baseTexture = baseTexture;

        if (enableArmorLayer) {
            this.addLayer(new HumanoidArmorLayer<>(
                    this,
                    new HumanoidArmorModel<>(ctx.bakeLayer(innerArmorLayer)),
                    new HumanoidArmorModel<>(ctx.bakeLayer(outerArmorLayer)),
                    ctx.getModelManager()
            ));
        }
    }

    protected AbstractPiglinLikeRenderer(
            EntityRendererProvider.Context ctx,
            M model,
            ModelLayerLocation innerArmorLayer,
            ModelLayerLocation outerArmorLayer,
            float shadowRadius,
            float customHeadScaleX,
            float customHeadScaleY,
            float customHeadScaleZ,
            ResourceLocation baseTexture,
            boolean enableArmorLayer
    ) {
        super(ctx, model, shadowRadius, customHeadScaleX, customHeadScaleY, customHeadScaleZ);
        this.baseTexture = baseTexture;

        if (enableArmorLayer) {
            this.addLayer(new HumanoidArmorLayer<>(
                    this,
                    new HumanoidArmorModel<>(ctx.bakeLayer(innerArmorLayer)),
                    new HumanoidArmorModel<>(ctx.bakeLayer(outerArmorLayer)),
                    ctx.getModelManager()
            ));
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return baseTexture;
    }
}
