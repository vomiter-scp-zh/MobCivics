package com.vomiter.mobcivics.api.common.entity;

import net.minecraft.world.entity.LivingEntity;

public interface IVillagerThreatEntity {
    default double villagerFearRadius() { return 8.0D; }
    default int villagerFearMemoryTtlTicks() { return 40; }
    default int villagerFearScanIntervalTicks() {
        int ttl = villagerFearMemoryTtlTicks();
        return Math.max(5, Math.min(20, ttl / 2));
    }
    default boolean villagerFearRequiresLineOfSight() { return true; }

    /** 只有回傳 true 才會寫 NEAREST_HOSTILE */
    default boolean villagerFearEnabled(LivingEntity self) {
        return true;
    }
}
