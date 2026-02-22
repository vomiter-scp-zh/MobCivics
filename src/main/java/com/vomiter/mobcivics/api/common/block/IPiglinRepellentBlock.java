package com.vomiter.mobcivics.api.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IPiglinRepellentBlock {
    boolean isRepellent(Level level, BlockPos pos);
}
