// MobCivicsAttachments.java
package com.vomiter.mobcivics.common.registry;

import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MobCivicsAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "mobcivics");

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<VillagerVisualState>> VILLAGER_VISUAL =
            ATTACHMENTS.register("villager_visual", () ->
                    AttachmentType.builder(VillagerVisualState::new)
                            .serialize(new IAttachmentSerializer<CompoundTag, VillagerVisualState>() {
                                @Override
                                public @NotNull VillagerVisualState read(@NotNull IAttachmentHolder iAttachmentHolder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
                                    VillagerVisualState data = new VillagerVisualState();
                                    if (tag != null && !tag.isEmpty()) data.deserializeNBT(tag);
                                    return data;
                                }

                                @Override
                                public @Nullable CompoundTag write(@NotNull VillagerVisualState state, HolderLookup.@NotNull Provider provider) {
                                    return (state == null || state.isDefault()) ? new CompoundTag() : state.serializeNBT();
                                }
                            })
                            .build()
            );

    private MobCivicsAttachments() {}
}