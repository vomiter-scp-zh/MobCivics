package com.vomiter.mobcivics.common.event;

import com.vomiter.mobcivics.common.service.ai.VillagerThreatHandler;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandler {
    public static void init(){
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(EventHandler::onLivingTick);
        bus.addGenericListener(Entity.class , VillagerVisualEvents::onAttachCaps);
        bus.addListener(VillagerVisualEvents::onStartTracking);
        bus.addListener(VillagerVisualEvents::onInteractionTest);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {

    }

    public static void onLivingTick(LivingEvent.LivingTickEvent event){
        VillagerThreatHandler.onLivingTick(event.getEntity());
    }


}
