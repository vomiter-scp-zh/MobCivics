package com.vomiter.mobcivics.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vomiter.mobcivics.api.client.IVillagerClothesVariantResolver;
import com.vomiter.mobcivics.client.villager.VillagerClothesTextureResolver;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerData;

import static com.vomiter.mobcivics.client.layer.ClothesLayerHelpers.*;

public class VillagerClothesLayerRenderer {

    private final String namespace;
    private final boolean renderLevelBadge;
    private final IVillagerClothesVariantResolver<?> variantResolver;

    public VillagerClothesLayerRenderer(
            String namespace,
            boolean renderLevelBadge,
            IVillagerClothesVariantResolver<?> variantResolver
    ) {
        this.namespace = namespace;
        this.renderLevelBadge = renderLevelBadge;
        this.variantResolver = variantResolver;
    }

    public <T extends Mob> void renderClothes(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity,
            EntityModel<T> parentModel,
            VillagerData data
    ) {
        if (data == null) {
            warnOnce("no_data:" + getType(entity),
                    "[MobCivics] VillagerData is null for entity type {}. Skip clothes layer (fallback to base renderer).",
                    getType(entity));
            return;
        }

        String variantKey = safeVariantKey(entity);

        VillagerClothesTextureResolver primary = new VillagerClothesTextureResolver(namespace, variantKey);
        boolean zombieLike = isZombieVillagerLike(entity);

        // type
        renderOneWithFixedVanillaFallback(
                poseStack, buffer, packedLight,
                entity, parentModel,
                primary.typeTexture(getTypeId(entity)),
                vanillaTypeTexture(getTypeId(entity), zombieLike)
        );

        // profession
        renderOneWithFixedVanillaFallback(
                poseStack, buffer, packedLight,
                entity, parentModel,
                primary.professionTexture(data.getProfession()),
                vanillaProfessionTexture(data.getProfession(), zombieLike)
        );

        // level badge
        if (renderLevelBadge) {
            renderOneWithFixedVanillaFallback(
                    poseStack, buffer, packedLight,
                    entity, parentModel,
                    primary.levelTexture(data.getLevel()),
                    vanillaLevelTexture(data.getLevel(), zombieLike)
            );
        }
    }

    @SuppressWarnings("unchecked")
    <T extends Mob> String safeVariantKey(T entity) {
        try {
            if (variantResolver == null) return "default";
            String key = ((IVillagerClothesVariantResolver<T>) variantResolver).mobcivics$getVariantKey(entity);
            if (key == null || key.isBlank()) return "default";
            return key;
        } catch (Throwable t) {
            warnOnce("variant_err:" + getType(entity),
                    "[MobCivics] Variant resolver failed for entity type {}. Use default variant. Err={}",
                    getType(entity), t.toString());
            return "default";
        }
    }

    static <T extends Mob> void renderOneWithFixedVanillaFallback(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity,
            EntityModel<T> parentModel,
            ResourceLocation preferred,
            ResourceLocation vanillaFallback
    ) {
        if (exists(preferred)) {
            renderOne(poseStack, buffer, packedLight, parentModel, preferred);
            return;
        }

        warnOnce("missing:" + preferred,
                "[MobCivics] Missing texture {} (type={}). Fallback to vanilla: {}",
                preferred, getType(entity), vanillaFallback);

        if (exists(vanillaFallback)) {
            renderOne(poseStack, buffer, packedLight, parentModel, vanillaFallback);
            return;
        }

        warnOnce("missing_vanilla:" + vanillaFallback,
                "[MobCivics] Vanilla fallback texture also missing: {} (type={}). Skip this layer.",
                vanillaFallback, getType(entity));
    }

    static <T extends Entity> void renderOne(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            EntityModel<T> parentModel,
            ResourceLocation texture
    ) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        parentModel.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );
    }
}