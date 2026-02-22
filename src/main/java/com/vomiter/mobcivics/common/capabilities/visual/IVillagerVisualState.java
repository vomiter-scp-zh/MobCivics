package com.vomiter.mobcivics.common.capabilities.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface IVillagerVisualState {

    boolean isHideProfessionLayer();
    void setHideProfessionLayer(boolean hide);

    boolean isHideHeadLayer();
    void setHideHeadLayer(boolean hide);

    boolean isHideHeldItemLayer();
    void setHideHeldItemLayer(boolean hide);

    // 若有自訂服裝 layer，可加一個開關
    boolean isShowCustomAttireLayer();
    void setShowCustomAttireLayer(boolean show);

    // NBT 序列化（cap 標準做法）
    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag tag);

    ResourceLocation getCustomTypeId();
    void setCustomTypeId(ResourceLocation id);

}
