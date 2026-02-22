package com.vomiter.mobcivics;

import com.mojang.logging.LogUtils;
import com.vomiter.mobcivics.client.ClientModEvents;
import com.vomiter.mobcivics.common.event.EventHandler;
import com.vomiter.mobcivics.common.event.ModEvents;
import com.vomiter.mobcivics.common.registry.ModRegistries;
import com.vomiter.mobcivics.data.ModDataGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

@Mod(MobCivics.MOD_ID)
public class MobCivics {
    public static final String MOD_ID = "mobcivics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MobCivics(FMLJavaModLoadingContext context) {
        EventHandler.init();
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModEvents.init(modBus);
        modBus.addListener(ModDataGenerator::generateData);
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
