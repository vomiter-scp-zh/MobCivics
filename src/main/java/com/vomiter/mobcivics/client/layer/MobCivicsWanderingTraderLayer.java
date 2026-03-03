package com.vomiter.mobcivics.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.MobCivics;
import com.vomiter.mobcivics.api.client.IVillagerClothesVariantResolver;
import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class MobCivicsWanderingTraderLayer<
        T extends Mob & IVillagerDataHolder,
        M extends HumanoidModel<T>
        > extends MobCivicsVillagerClothesLayer<T, M> {
    private final ResourceLocation resourceLocation;

    public MobCivicsWanderingTraderLayer(
            RenderLayerParent<T, M> parent,
            ResourceLocation resourceLocation
    ) {
        super(parent, "undeadvariants", false, e -> ""); // renderLevelBadge=false
        this.resourceLocation = resourceLocation;
        MobCivics.LOGGER.info("[MobCivics] Wandering Trader Texture:{}", resourceLocation);
    }

    @Override
    public void render(
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource buffer,
            int packedLight,
            @NotNull T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // 可選：你若仍想在缺 data 時跳過（保守）
        if (entity.mobcivics$getVillagerData() == null) return;

        // fallback 你自己決定：要嘛給一張你 mod 內的備援貼圖，要嘛直接不畫
        // 這裡示範 fallback 到 vanilla 的某張你指定的 trader-like 材質（如果你 helpers 有提供就用）
        ResourceLocation fallback = Helpers.textureId(Helpers.minecraftId("wandering_trader")); // 你可能需要在 helpers 補一個
        VillagerClothesLayerRenderer.renderOneWithFixedVanillaFallback(poseStack, buffer, packedLight, entity, getParentModel(), resourceLocation, fallback);
    }
}