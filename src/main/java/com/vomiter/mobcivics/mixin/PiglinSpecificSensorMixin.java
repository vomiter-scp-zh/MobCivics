package com.vomiter.mobcivics.mixin;

import com.vomiter.mobcivics.common.service.ai.PiglinAIHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {
    @Inject(method = "isValidRepellent", at = @At("HEAD"), cancellable = true)
    private static void extraRepellent(ServerLevel level, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if(PiglinAIHooks.isExtraRepellent(level, pos)) cir.setReturnValue(true);
    }
}
