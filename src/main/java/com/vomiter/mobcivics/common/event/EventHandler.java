package com.vomiter.mobcivics.common.event;

import com.vomiter.mobcivics.common.service.ai.VillagerThreatHandler;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class EventHandler {
    public static void init(){
        final IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(EventHandler::onLivingTick);
        bus.addListener(VillagerVisualEvents::onStartTracking);
        bus.addListener(VillagerVisualEvents::onInteractionTest);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {

    }

    public static void onLivingTick(EntityTickEvent.Pre event){
        var entity = event.getEntity();
        if(entity instanceof LivingEntity living){
            VillagerThreatHandler.onLivingTick(living);
        }
    }


}
