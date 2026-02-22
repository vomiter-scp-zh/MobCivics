// VillagerVisualSyncS2C.java (NeoForge 1.21.x)
package com.vomiter.mobcivics.network;

import com.vomiter.mobcivics.Helpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record VillagerVisualSyncS2C(int entityId, CompoundTag tag) implements INetWorkPacket, CustomPacketPayload {

    // 每個 payload 需要一個唯一的 Type（你的 channel 名稱概念現在落在這裡）
    public static final Type<VillagerVisualSyncS2C> TYPE =
            new Type<>(Helpers.mobCivicsId("villager_visual_sync"));

    // StreamCodec：取代你原本的 encode/decode
    public static final StreamCodec<RegistryFriendlyByteBuf, VillagerVisualSyncS2C> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, VillagerVisualSyncS2C::entityId,
                    ByteBufCodecs.COMPOUND_TAG, VillagerVisualSyncS2C::tag,
                    VillagerVisualSyncS2C::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // handler：用 ctx.enqueueWork 切回主執行緒（等價於你原本 consumerMainThread）
    public static void handle(VillagerVisualSyncS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (MobCivicsNetwork.CLIENT != null) {
                MobCivicsNetwork.CLIENT.handle(msg, () -> ctx);
            }
        });
    }
}