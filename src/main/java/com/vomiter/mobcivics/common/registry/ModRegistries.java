package com.vomiter.mobcivics.common.registry;

import com.vomiter.mobcivics.MobCivics;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModRegistries {
    static List<DeferredRegister<?>> REGISTRIES = new ArrayList<>();
    public static <B> DeferredRegister<B> createRegistry(Registry<B> b){
        return DeferredRegister.create(b, MobCivics.MOD_ID);
    }
    public static <B> DeferredRegister<B> createRegistry(ResourceKey b){
        return DeferredRegister.create(b, MobCivics.MOD_ID);
    }

    static void add(DeferredRegister<?> r){
        REGISTRIES.add(r);
    }

    public static void register(IEventBus modBus){
        add(ModItems.ITEMS);
        add(ModBlockEntities.BLOCK_ENTITIES);
        add(MobCivicsAttachments.ATTACHMENTS);
        for (DeferredRegister<?> registry : REGISTRIES) {
            registry.register(modBus);
        }
    }
}
