package com.vomiter.mobcivics.common.event;

import com.vomiter.mobcivics.network.MobCivicsNetwork;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ModEvents {

    public static void init(IEventBus modBus){
        modBus.addListener(MobCivicsNetwork::onCommonSetup);
    }

}
