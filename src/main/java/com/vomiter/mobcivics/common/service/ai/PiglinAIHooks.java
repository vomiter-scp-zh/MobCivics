package com.vomiter.mobcivics.common.service.ai;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.api.common.block.IPiglinRepellentBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PiglinAIHooks {
    public static boolean isExtraRepellent(Level level, BlockPos pos){
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if(block instanceof IPiglinRepellentBlock piglinRepellentBlock){
            return piglinRepellentBlock.isRepellent(level, pos);
        }
        if(!optionalRepellent.isEmpty()){
            var rl = BuiltInRegistries.BLOCK.getKey(block);
            var func = getOptionalRepellentFunction(rl);
            if(func != null) return func.apply(state);
        }
        return false;
    }

    public static boolean isExtraZombified(EntityType<?> entityType){
        return entityType.is(ZOMBIES_THAT_PIGLINS_FEAR);
    }

    private static final TagKey<EntityType<?>> ZOMBIES_THAT_PIGLINS_FEAR =
            TagKey.create(Registries.ENTITY_TYPE, Helpers.mobCivicsId("zombies_that_piglins_fear"));

    private static final Map<ResourceLocation, Function<BlockState, Boolean>> optionalRepellent
            = new HashMap<>();

    public static Function<BlockState, Boolean> getOptionalRepellentFunction(ResourceLocation rl) {
        return optionalRepellent.getOrDefault(rl, (state0) -> false);
    }

    public static void registerOptionalRepellent(ResourceLocation rl, Function<BlockState, Boolean> function){
        optionalRepellent.put(rl, function);
    }
}
