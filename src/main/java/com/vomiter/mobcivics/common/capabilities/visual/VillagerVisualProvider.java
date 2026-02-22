package com.vomiter.mobcivics.common.capabilities.visual;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VillagerVisualProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    private VillagerVisualState backend; // nullable

    private final LazyOptional<IVillagerVisualState> opt =
            LazyOptional.of(() -> getOrCreateBackend());

    private VillagerVisualState getOrCreateBackend() {
        if (backend == null) backend = new VillagerVisualState();
        return backend;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == VillagerVisualState.VILLAGER_VISUAL ? opt.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        // 沒資料就回傳空，讓 entity NBT 不會出現你的 key
        if (backend == null || backend.isDefault()) return new CompoundTag();
        return backend.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // 沒內容就不要建立 backend，保持真正的 lazy
        if (nbt == null || nbt.isEmpty()) return;
        getOrCreateBackend().deserializeNBT(nbt);
    }

    public void invalidate() { opt.invalidate(); }
}
