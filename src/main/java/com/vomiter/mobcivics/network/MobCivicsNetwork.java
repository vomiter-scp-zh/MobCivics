package com.vomiter.mobcivics.network;

import com.vomiter.mobcivics.Helpers;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MobCivicsNetwork {
    private static final String PROTOCOL = "1";
    public static SimpleChannel CHANNEL;
    private static boolean initialized = false;
    public static IMobCivicsClientNetworkHandler CLIENT;

    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(MobCivicsNetwork::init); // 兩端都會跑，註冊 message handler
    }

    public static void init() {
        if (initialized) return;
        if(FMLEnvironment.dist.isClient()) MobCivicsNetwork.CLIENT = new MobCivicsClientNetworkHandler();
        initialized = true;
        CHANNEL = NetworkRegistry.newSimpleChannel(
                Helpers.mobCivicsId("main"),
                () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
        );

        int id = 0;

        CHANNEL.messageBuilder(VillagerVisualSyncS2C.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(VillagerVisualSyncS2C::encode)
                .decoder(VillagerVisualSyncS2C::decode)
                .consumerMainThread(VillagerVisualSyncS2C::handle)
                .add();
    }
}
