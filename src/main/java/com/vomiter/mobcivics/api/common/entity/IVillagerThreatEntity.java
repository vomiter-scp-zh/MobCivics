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

    /** LivingEntity threatEntity: The threat entity.*/
    default boolean villagerFearEnabled(LivingEntity threatEntity) {
        return true;
    }
    default boolean villagerFearEnabled() {
        return villagerFearEnabled((LivingEntity) this);
    }


}
