package com.vomiter.mobcivics.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMobCivicsClientNetworkHandler {
    void handle(INetWorkPacket msg, Supplier<NetworkEvent.Context> ctx);
}
