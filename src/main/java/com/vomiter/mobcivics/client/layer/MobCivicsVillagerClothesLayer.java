package com.vomiter.mobcivics.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vomiter.mobcivics.api.client.IVillagerClothesVariantResolver;
import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class MobCivicsVillagerClothesLayer<
        T extends Mob & IVillagerDataHolder,
        M extends HumanoidModel<T>
        > extends RenderLayer<T, M> {

    private final VillagerClothesLayerRenderer renderer;

    public MobCivicsVillagerClothesLayer(
            RenderLayerParent<T, M> parent,
            String namespace,
            boolean renderLevelBadge,
            IVillagerClothesVariantResolver<T> variantResolver
    ) {
        super(parent);
        this.renderer = new VillagerClothesLayerRenderer(namespace, renderLevelBadge, variantResolver);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {

        renderer.renderClothes(
                poseStack, buffer, packedLight,
                entity,
                this.getParentModel(),
                entity.mobcivics$getVillagerData()
        );
    }
}