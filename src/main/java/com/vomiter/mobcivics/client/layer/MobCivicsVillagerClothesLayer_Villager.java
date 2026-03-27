package com.vomiter.mobcivics.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vomiter.mobcivics.api.client.IVillagerClothesVariantResolver;
import com.vomiter.mobcivics.client.villager.VillagerClothesTextureResolver;
import com.vomiter.mobcivics.common.registry.MobCivicsAttachments;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import org.jetbrains.annotations.NotNull;

import static com.vomiter.mobcivics.client.layer.ClothesLayerHelpers.*;

public class MobCivicsVillagerClothesLayer_Villager<
        T extends Villager,
        M extends VillagerModel<T>
        > extends RenderLayer<T, M> {

    private final VillagerClothesLayerRenderer renderer;

    public MobCivicsVillagerClothesLayer_Villager(
            RenderLayerParent<T, M> parent,
            String namespace,
            boolean renderLevelBadge,
            IVillagerClothesVariantResolver<T> variantResolver
    ) {
        super(parent);
        this.renderer = new VillagerClothesLayerRenderer(namespace, renderLevelBadge, variantResolver);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {

        if (entity.getType() == EntityType.VILLAGER) {
            if (!VillagerVisualState.getOrDefault(entity).isShowCustomAttireLayer()) return;
        }

        renderer.renderClothes(
                poseStack, buffer, packedLight,
                entity,
                this.getParentModel(),
                entity.getVillagerData()
        );
    }
}