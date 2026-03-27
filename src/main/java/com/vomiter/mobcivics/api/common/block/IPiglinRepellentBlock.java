package com.vomiter.mobcivics.api.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.Level;

public interface IPiglinRepellentBlock {
    default boolean isRepellent(Level level, BlockPos pos) {
        return false;
    }

    default boolean isRepellent(Level level, BlockPos pos, Piglin piglin) {
        return isRepellent(level, pos);
    }
}