package com.vomiter.mobcivics.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerProfessionLayer.class)
public class VillagerProfessionLayerMixin {
    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD")
            , cancellable = true
    )
    private void setVanillaInvisible(
            PoseStack p_117646_,
            MultiBufferSource p_117647_,
            int p_117648_,
            LivingEntity livingEntity,
            float p_117650_,
            float p_117651_,
            float p_117652_,
            float p_117653_,
            float p_117654_,
            float p_117655_,
            CallbackInfo ci){
        if(livingEntity instanceof Villager villager){
            if(VillagerVisualState.getOrDefault(villager).isShowCustomAttireLayer()) ci.cancel();
        }
    }

}
