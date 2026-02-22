package com.vomiter.mobcivics.common.event;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class EventHandler {
    public static void init(){
        final IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(VillagerFearHandler::onLivingTick);
        bus.addListener(VillagerVisualHandler::onStartTracking);
        bus.addListener(VillagerVisualHandler::onInteractionTest);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {

    }


}
