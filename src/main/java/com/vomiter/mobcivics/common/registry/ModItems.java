package com.vomiter.mobcivics.common.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    static DeferredRegister<Item> ITEMS
            = ModRegistries.createRegistry(ForgeRegistries.ITEMS);

    public static final RegistryObject<Item> TEST_WAND =
            ITEMS.register("test_wand", () ->
                    new Item(new Item.Properties())
            );
}
