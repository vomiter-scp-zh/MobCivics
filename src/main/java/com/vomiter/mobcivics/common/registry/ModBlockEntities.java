package com.vomiter.mobcivics.common.registry;

import com.vomiter.mobcivics.common.block.blockentity.SkullLikeBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES
            = ModRegistries.createRegistry(ForgeRegistries.BLOCK_ENTITY_TYPES);
    public static List<Supplier<Block>> validHeads = new ArrayList<>();
    public static final RegistryObject<BlockEntityType<SkullLikeBlockEntity>> SKULL_LIKE = BLOCK_ENTITIES.register(
            "skull_like",
            () -> BlockEntityType.Builder.of(
                    SkullLikeBlockEntity::new,
                    validHeads.stream().map(Supplier::get).toArray(Block[]::new)
            ).build(null)
    );

}
