package com.vomiter.mobcivics.common.registry;

import com.vomiter.mobcivics.common.block.blockentity.SkullLikeBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES
            = ModRegistries.createRegistry(BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static List<Supplier<Block>> validHeads = new ArrayList<>();
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SkullLikeBlockEntity>> SKULL_LIKE = BLOCK_ENTITIES.register(
            "skull_like",
            () -> BlockEntityType.Builder.of(
                    SkullLikeBlockEntity::new,
                    validHeads.stream().map(Supplier::get).toArray(Block[]::new)
            ).build(null)
    );

}
