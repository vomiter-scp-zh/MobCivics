package com.vomiter.mobcivics.mixin;

import com.vomiter.mobcivics.common.service.ai.PiglinAIHooks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PiglinAi.class)
public class PiglinAiMixin {
    @Inject(method = "isZombified", at =@At("HEAD"), cancellable = true)
    private static void zombifiedByTag(EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir){
        if(PiglinAIHooks.isExtraZombified(entityType)){
            cir.setReturnValue(true);
        }
    }


}
