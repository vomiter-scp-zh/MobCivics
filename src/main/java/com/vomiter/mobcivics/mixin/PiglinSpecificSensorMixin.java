package com.vomiter.mobcivics.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.mobcivics.api.common.entity.IPiglinFearEntity;
import com.vomiter.mobcivics.common.service.ai.PiglinAIHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {

    @WrapOperation(
            method = "findNearestRepellent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;findClosestMatch(Lnet/minecraft/core/BlockPos;IILjava/util/function/Predicate;)Ljava/util/Optional;"
            )
    )
    private static Optional<BlockPos> mobcivics$findClosestMatchWithPiglin(
            BlockPos center,
            int rangeX,
            int rangeY,
            Predicate<BlockPos> originalPredicate,
            Operation<Optional<BlockPos>> original,
            ServerLevel level,
            LivingEntity livingEntity
    ) {
        if (!(livingEntity instanceof Piglin piglin)) {
            return original.call(center, rangeX, rangeY, originalPredicate);
        }

        Predicate<BlockPos> wrapped = pos ->
                originalPredicate.test(pos) || PiglinAIHooks.isExtraRepellent(level, pos, piglin);

        return original.call(center, rangeX, rangeY, wrapped);
    }

    @Inject(method = "doTick", at = @At("TAIL"))
    private void extraFear(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfo ci) {
        if (!(livingEntity instanceof Piglin)) return;

        var brain = livingEntity.getBrain();
        var nearestZombie = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED);
        if (nearestZombie.isPresent()) return;

        var firstVisibleExtraZombie = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .orElse(NearestVisibleLivingEntities.empty())
                .findClosest(visible -> visible instanceof IPiglinFearEntity piglinFear && piglinFear.piglinShouldFear());

        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, firstVisibleExtraZombie);
    }
}