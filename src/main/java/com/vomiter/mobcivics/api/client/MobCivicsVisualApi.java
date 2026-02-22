// MobCivicsVisualApi.java
package com.vomiter.mobcivics.api.client;

import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import com.vomiter.mobcivics.common.registry.MobCivicsAttachments;
import com.vomiter.mobcivics.network.VillagerVisualSyncS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public final class MobCivicsVisualApi {

    public static VillagerVisualState getOrCreate(LivingEntity entity) {
        return entity.getData(MobCivicsAttachments.VILLAGER_VISUAL.get());
    }

    public static void setCustomType(LivingEntity entity, ResourceLocation id) {
        VillagerVisualState state = getOrCreate(entity);
        state.setCustomTypeId(id);
        syncIfServer(entity, state);
    }

    public static void clearCustomType(LivingEntity entity) {
        VillagerVisualState state = getOrCreate(entity);
        state.setCustomTypeId(null);
        syncIfServer(entity, state);
    }

    public static void sync(LivingEntity entity) {
        VillagerVisualState state = getOrCreate(entity);
        syncIfServer(entity, state);
    }

    private static void syncIfServer(LivingEntity entity, VillagerVisualState state) {
        if (!(entity.level() instanceof ServerLevel)) return;

        // 你的 S2C payload 內容可以沿用：entityId + state.serializeNBT()
        PacketDistributor.sendToPlayersTrackingEntity(
                entity,
                new VillagerVisualSyncS2C(entity.getId(), state.serializeNBT())
        );
    }

    private MobCivicsVisualApi() {}
}