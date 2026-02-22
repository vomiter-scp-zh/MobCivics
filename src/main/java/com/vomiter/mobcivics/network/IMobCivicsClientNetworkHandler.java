package com.vomiter.mobcivics.network;


import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public interface IMobCivicsClientNetworkHandler {
    void handle(INetWorkPacket msg, Supplier<IPayloadContext> ctx);
}
