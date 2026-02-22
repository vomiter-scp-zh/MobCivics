// MobCivicsNetwork.java (NeoForge 1.21.x)
package com.vomiter.mobcivics.network;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class MobCivicsNetwork {
    public static final String PROTOCOL = "1";

    public static IMobCivicsClientNetworkHandler CLIENT;

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        // 版本字串：伺服器/客戶端不一致會直接握手失敗
        final PayloadRegistrar registrar = event.registrar(PROTOCOL);

        // 你的 client handler 初始化（也可以挪去更合適的 client-only init）
        if (FMLEnvironment.dist == Dist.CLIENT && CLIENT == null) {
            CLIENT = new MobCivicsClientNetworkHandler();
        }

        // 註冊：S2C
        registrar.playToClient(
                VillagerVisualSyncS2C.TYPE,
                VillagerVisualSyncS2C.STREAM_CODEC,
                VillagerVisualSyncS2C::handle
        );
    }

    private MobCivicsNetwork() {}
}