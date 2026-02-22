package com.vomiter.mobcivics.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vomiter.mobcivics.api.client.IVillagerClothesVariantResolver;
import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import com.vomiter.mobcivics.client.villager.VillagerClothesTextureResolver;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerData;
import org.jetbrains.annotations.NotNull;

import static com.vomiter.mobcivics.client.layer.ClothesLayerHelpers.*;

public class MobCivicsVillagerClothesLayer<
        T extends Mob & IVillagerDataHolder,
        M extends HumanoidModel<T>
        > extends RenderLayer<T, M> {

    private final String namespace;
    private final boolean renderLevelBadge;
    private final IVillagerClothesVariantResolver<T> variantResolver;

    public MobCivicsVillagerClothesLayer(
            RenderLayerParent<T, M> parent,
            String namespace,
            boolean renderLevelBadge,
            IVillagerClothesVariantResolver<T> variantResolver
    ) {
        super(parent);
        this.namespace = namespace;
        this.renderLevelBadge = renderLevelBadge;
        this.variantResolver = variantResolver;
    }


    @Override
    public void render(
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource buffer,
            int packedLight,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        VillagerData data = entity.mobcivics$getVillagerData();
        if (data == null) {
            warnOnce("no_data:" + getType(entity),
                    "[MobCivics] VillagerData is null for entity type {}. Skip clothes layer (fallback to base renderer).",
                    getType(entity));
            return;
        }

        String variantKey = safeVariantKey(entity);

        // primary：你自訂 namespace + variant 結構
        VillagerClothesTextureResolver primary = new VillagerClothesTextureResolver(namespace, variantKey);

        boolean zombieLike = isZombieVillagerLike(entity);

        // type layer
        renderOneWithFixedVanillaFallback(
                poseStack, buffer, packedLight,
                entity,
                primary.typeTexture(getTypeId(entity)),
                vanillaTypeTexture(getTypeId(entity), zombieLike)
        );

        // profession layer
        renderOneWithFixedVanillaFallback(
                poseStack, buffer, packedLight,
                entity,
                primary.professionTexture(data.getProfession()),
                vanillaProfessionTexture(data.getProfession(), zombieLike)
        );

        // level badge
        if (renderLevelBadge) {
            renderOneWithFixedVanillaFallback(
                    poseStack, buffer, packedLight,
                    entity,
                    primary.levelTexture(data.getLevel()),
                    vanillaLevelTexture(data.getLevel(), zombieLike)
            );
        }
    }

    private String safeVariantKey(T entity) {
        try {
            String key = variantResolver != null ? variantResolver.mobcivics$getVariantKey(entity) : null;
            if (key == null || key.isBlank()) return "default";
            return key;
        } catch (Throwable t) {
            warnOnce("variant_err:" + getType(entity),
                    "[MobCivics] Variant resolver failed for entity type {}. Use default variant. Err={}",
                    getType(entity), t.toString());
            return "default";
        }
    }

    private void renderOneWithFixedVanillaFallback(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity,
            ResourceLocation preferred,
            ResourceLocation vanillaFallback
    ) {
        // 先畫 preferred（你的 namespace）
        if (exists(preferred)) {
            renderOne(poseStack, buffer, packedLight, preferred);
            return;
        }

        warnOnce("missing:" + preferred,
                "[MobCivics] Missing texture {} (type={}). Fallback to vanilla: {}",
                preferred, getType(entity), vanillaFallback);

        // 固定 vanilla 路徑
        if (exists(vanillaFallback)) {
            renderOne(poseStack, buffer, packedLight, vanillaFallback);
            return;
        }

        warnOnce("missing_vanilla:" + vanillaFallback,
                "[MobCivics] Vanilla fallback texture also missing: {} (type={}). Skip this layer.",
                vanillaFallback, getType(entity));
    }

    private void renderOne(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ResourceLocation texture) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        this.getParentModel().renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f
        );
    }

}
