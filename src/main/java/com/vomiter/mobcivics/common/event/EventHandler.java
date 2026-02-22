package com.vomiter.mobcivics.common.event;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandler {
    public static void init(){
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(VillagerFearHandler::onLivingTick);
        bus.addGenericListener(Entity.class , VillagerVisualHandler::onAttachCaps);
        bus.addListener(VillagerVisualHandler::onStartTracking);
        bus.addListener(VillagerVisualHandler::onInteractionTest);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {

    }


}
