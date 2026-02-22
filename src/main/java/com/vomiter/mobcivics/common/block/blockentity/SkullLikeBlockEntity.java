package com.vomiter.mobcivics.common.block.blockentity;

import com.vomiter.mobcivics.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkullLikeBlockEntity extends BlockEntity {

    public SkullLikeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SKULL_LIKE.get(), pos, state);
    }
}
