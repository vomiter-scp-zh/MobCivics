package com.vomiter.mobcivics.client;

import com.vomiter.mobcivics.client.layer.MobCivicsVillagerClothesLayer_Villager;
import com.vomiter.mobcivics.client.renderer.SkullLikeRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ClientModEvents {
    public static void init(IEventBus modBus){
        modBus.addListener(ClientModEvents::onClientSetup);
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        modBus.addListener(ClientModEvents::onAddLayers);
        modBus.addListener(SkullLikeRenderer::onRegisterRenderers);
    }

    public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
    }

    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {

        // 1) 原版 Villager
        var villagerRenderer = event.getEntityRenderer(EntityType.VILLAGER);
        if (villagerRenderer instanceof VillagerRenderer vr) {
            // 注意：你目前這個 layer 的 HumanoidModel 泛型不適用 VillagerRenderer
            // 這裡應該加「Villager 專用版本」(下面第 2 節會給)
            vr.addLayer(new MobCivicsVillagerClothesLayer_Villager<>(
                    vr,
                    "mobcivics",
                    true,
                    villager -> "default"
            ));
        }

        /*
        // 2) Zombie Villager（這個通常是 HumanoidModel，較可能直接相容）
        var zombieVillagerRenderer = event.getRenderer(EntityType.ZOMBIE_VILLAGER);
        if (zombieVillagerRenderer instanceof ZombieVillagerRenderer zvr) {

            // 如果你的 ZombieVillager 已經透過 mixin 實作 IVillagerDataHolder，
            // 那你原本那個 MobCivicsVillagerClothesLayer 就能直接上
            zvr.addLayer(new MobCivicsVillagerClothesLayer<>(
                    zvr,
                    "mobcivics",
                    true,
                    (zombie) -> "default"
            ));
        }

         */
    }


}
