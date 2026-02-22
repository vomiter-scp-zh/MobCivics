package com.vomiter.mobcivics.common.capabilities.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public final class DefaultVillagerVisualState implements IVillagerVisualState {

    public static final DefaultVillagerVisualState INSTANCE =
            new DefaultVillagerVisualState();

    private DefaultVillagerVisualState() {}

    @Override public boolean isHideProfessionLayer() { return false; }
    @Override public void setHideProfessionLayer(boolean hide) {}

    @Override public boolean isHideHeadLayer() { return false; }
    @Override public void setHideHeadLayer(boolean hide) {}

    @Override public boolean isHideHeldItemLayer() { return false; }
    @Override public void setHideHeldItemLayer(boolean hide) {}

    @Override public boolean isShowCustomAttireLayer() { return false; }
    @Override public void setShowCustomAttireLayer(boolean show) {}

    @Override public ResourceLocation getCustomTypeId() { return null; }
    @Override public void setCustomTypeId(ResourceLocation id) {}

    @Override public CompoundTag serializeNBT() {
        return new CompoundTag(); // 永遠空
    }

    @Override public void deserializeNBT(CompoundTag tag) {}
}
