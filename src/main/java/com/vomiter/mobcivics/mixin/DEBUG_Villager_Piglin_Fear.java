package com.vomiter.mobcivics.mixin;

import com.vomiter.mobcivics.api.common.entity.IPiglinFearEntity;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Villager.class)
public class DEBUG_Villager_Piglin_Fear implements IPiglinFearEntity {
    @Override
    public boolean piglinShouldFear() {
        return true;
    }
}
