package com.vomiter.mobcivics.common.capabilities.visual;

import com.vomiter.mobcivics.Helpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class VillagerVisualState implements IVillagerVisualState {
    public static final ResourceLocation KEY = Helpers.mobCivicsId( "villager_visual");
    public static final Capability<IVillagerVisualState> VILLAGER_VISUAL =
            CapabilityManager.get(new CapabilityToken<>() {});

    private boolean hideProfessionLayer;
    private boolean hideHeadLayer;
    private boolean hideHeldItemLayer;
    private boolean showCustomAttireLayer;
    private ResourceLocation customTypeId;

    public boolean isDefault() {
        return !hideProfessionLayer
                && !hideHeadLayer
                && !hideHeldItemLayer
                && !showCustomAttireLayer
                && customTypeId == null;
    }

    @Override
    public ResourceLocation getCustomTypeId(){return customTypeId;}

    @Override
    public void setCustomTypeId(ResourceLocation id){
        customTypeId = id;
    }


    @Override
    public boolean isHideProfessionLayer() { return hideProfessionLayer; }

    @Override
    public void setHideProfessionLayer(boolean hide) { this.hideProfessionLayer = hide; }

    @Override
    public boolean isHideHeadLayer() { return hideHeadLayer; }

    @Override
    public void setHideHeadLayer(boolean hide) { this.hideHeadLayer = hide; }

    @Override
    public boolean isHideHeldItemLayer() { return hideHeldItemLayer; }

    @Override
    public void setHideHeldItemLayer(boolean hide) { this.hideHeldItemLayer = hide; }

    @Override
    public boolean isShowCustomAttireLayer() { return showCustomAttireLayer; }

    @Override
    public void setShowCustomAttireLayer(boolean show) { this.showCustomAttireLayer = show; }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("hideProfession", hideProfessionLayer);
        tag.putBoolean("hideHead", hideHeadLayer);
        tag.putBoolean("hideHeldItem", hideHeldItemLayer);
        tag.putBoolean("showCustomAttire", showCustomAttireLayer);

        if (customTypeId != null) {
            tag.putString("customTypeId", customTypeId.toString());
        } else {
            // 可選：如果你還留著舊欄位，才需要這段；不留就不用
            // tag.putString("customType", ...);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        hideProfessionLayer = tag.getBoolean("hideProfession");
        hideHeadLayer = tag.getBoolean("hideHead");
        hideHeldItemLayer = tag.getBoolean("hideHeldItem");
        showCustomAttireLayer = tag.getBoolean("showCustomAttire");

        // 新欄位優先
        if (tag.contains("customTypeId")) {
            customTypeId = ResourceLocation.tryParse(tag.getString("customTypeId"));
        } else if (tag.contains("customType")) {
            // 舊欄位（你之前存的是 VillagerType 的 registry key 字串）
            customTypeId = ResourceLocation.tryParse(tag.getString("customType"));
        } else {
            customTypeId = null;
        }
    }

    public static IVillagerVisualState get(Villager villager) {
        return villager.getCapability(VILLAGER_VISUAL)
                .orElseThrow(() -> new IllegalStateException("Missing villager visual capability"));
    }

    public static boolean has(Villager villager) {
        return villager.getCapability(VILLAGER_VISUAL).isPresent();
    }

    public static IVillagerVisualState getOrDefault(Villager villager) {
        return villager.getCapability(VILLAGER_VISUAL)
                .filter(state -> {
                    if (state instanceof VillagerVisualState real) {
                        return !real.isDefault();
                    }
                    return true;
                })
                .orElse(DefaultVillagerVisualState.INSTANCE);
    }

    public static VillagerVisualState getOrCreate(Villager villager) {
        return villager.getCapability(VILLAGER_VISUAL)
                .map(cap -> (VillagerVisualState) cap)
                .orElseThrow(() -> new IllegalStateException("Villager visual cap missing"));
    }


}