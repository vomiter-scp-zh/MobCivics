package com.vomiter.mobcivics.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record VillagerVisualSyncS2C(int entityId, CompoundTag tag) implements INetWorkPacket{

    public static void encode(VillagerVisualSyncS2C msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityId);
        buf.writeNbt(msg.tag);
    }

    public static VillagerVisualSyncS2C decode(FriendlyByteBuf buf) {
        int id = buf.readVarInt();
        CompoundTag tag = buf.readNbt();
        if (tag == null) tag = new CompoundTag();
        return new VillagerVisualSyncS2C(id, tag);
    }

    public static void handle(VillagerVisualSyncS2C msg, Supplier<NetworkEvent.Context> ctx) {
        if(MobCivicsNetwork.CLIENT != null) MobCivicsNetwork.CLIENT.handle(msg, ctx);
    }

}
