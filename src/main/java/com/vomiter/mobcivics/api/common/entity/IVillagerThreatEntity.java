package com.vomiter.mobcivics.api.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;

public interface IVillagerThreatEntity {
    default double villagerFearRadius() { return 8.0D; }
    default int villagerFearMemoryTtlTicks() { return 40; }
    default int villagerFearScanIntervalTicks() {
        int ttl = villagerFearMemoryTtlTicks();
        return Math.max(5, Math.min(20, ttl / 2));
    }
    default boolean villagerFearRequiresLineOfSight() { return true; }

    default AbstractVillager getAbstractVillager(){
        if(this instanceof LivingEntity living){
            return new Villager(EntityType.VILLAGER, living.level());
        }
        return null;
    }

    @Deprecated
    /** LivingEntity threatEntity: The threat entity.*/
    default boolean villagerFearEnabled(LivingEntity threatEntity) {
        return false;
    }

    default boolean villagerFearEnabled() {
        if(this instanceof LivingEntity living) return villagerFearEnabled(living);
        return false;
    }

    default boolean villagerFearEnabled(AbstractVillager villager) {
        return villagerFearEnabled();
    }



}
