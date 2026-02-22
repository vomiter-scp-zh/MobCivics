package com.vomiter.mobcivics.common.registry;

import com.vomiter.mobcivics.MobCivics;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModRegistries {
    static List<DeferredRegister<?>> REGISTRIES = new ArrayList<>();
    public static <B> DeferredRegister<B> createRegistry(IForgeRegistry<B> b){
        return DeferredRegister.create(b, MobCivics.MOD_ID);
    }

    static void add(DeferredRegister<?> r){
        REGISTRIES.add(r);
    }

    public static void register(IEventBus modBus){
        add(ModItems.ITEMS);
        add(ModBlockEntities.BLOCK_ENTITIES);
        for (DeferredRegister<?> registry : REGISTRIES) {
            registry.register(modBus);
        }
    }
}
