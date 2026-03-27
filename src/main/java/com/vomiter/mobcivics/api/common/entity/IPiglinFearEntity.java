package com.vomiter.mobcivics.api.common.entity;

import net.minecraft.world.entity.monster.piglin.Piglin;

public interface IPiglinFearEntity {
    boolean piglinShouldFear();
    boolean piglinShouldFear(Piglin piglin);
}
