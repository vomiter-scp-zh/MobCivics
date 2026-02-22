package com.vomiter.mobcivics;

import com.mojang.logging.LogUtils;
import com.vomiter.mobcivics.client.ClientModEvents;
import com.vomiter.mobcivics.common.event.EventHandler;
import com.vomiter.mobcivics.common.registry.ModRegistries;
import com.vomiter.mobcivics.data.ModDataGenerator;
import com.vomiter.mobcivics.network.MobCivicsNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;

@Mod(MobCivics.MOD_ID)
public class MobCivics {
    public static final String MOD_ID = "mobcivics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MobCivics(ModContainer mod, IEventBus modBus) {
        EventHandler.init();
        modBus.addListener(this::commonSetup);
        mod.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modBus.addListener(ModDataGenerator::generateData);
        modBus.addListener(MobCivicsNetwork::onRegisterPayloadHandlers);
        ModRegistries.register(modBus);

        if(FMLLoader.getDist().isClient()){
            ClientModEvents.init(modBus);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }
}
