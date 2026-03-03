package com.vomiter.mobcivics.mixin;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {
    @Inject(method = "isValidRepellent", at = @At("HEAD"), cancellable = true)
    private static void extraRepellent(ServerLevel level, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if(PiglinAIHooks.isExtraRepellent(level, pos)) cir.setReturnValue(true);
    }

    @Inject(method = "doTick", at = @At("TAIL"))
    private void extraFear(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfo ci){
        if(!(livingEntity instanceof Piglin)) return;
        var brain = livingEntity.getBrain();
        var nearestZombie = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED);
        if(nearestZombie.isPresent()) return;
        var firstVisibleExtraZombie
                = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .orElse(NearestVisibleLivingEntities.empty())
                .findClosest(visible -> visible instanceof IPiglinFearEntity piglinFear && piglinFear.piglinShouldFear());
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, firstVisibleExtraZombie);
    }
}
