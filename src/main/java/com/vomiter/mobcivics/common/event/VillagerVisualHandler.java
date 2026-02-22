package com.vomiter.mobcivics.common.event;

import com.vomiter.mobcivics.common.capabilities.visual.IVillagerVisualState;
import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import com.vomiter.mobcivics.common.registry.MobCivicsAttachments;
import com.vomiter.mobcivics.common.registry.ModItems;
import com.vomiter.mobcivics.network.VillagerVisualSyncS2C;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;


public class VillagerVisualHandler {
    static void onInteractionTest(PlayerInteractEvent.EntityInteract event){
        if(!event.getEntity().getMainHandItem().is(ModItems.TEST_WAND.get())) return;
        if(!(event.getTarget() instanceof Villager villager)) return;
        IVillagerVisualState visualState = villager.getData(MobCivicsAttachments.VILLAGER_VISUAL.get());
        visualState.setShowCustomAttireLayer(true);
        VillagerType[] types = {VillagerType.DESERT, VillagerType.JUNGLE, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.SWAMP, VillagerType.TAIGA};
        if(visualState.getCustomTypeId() == null) visualState.setCustomTypeId(BuiltInRegistries.VILLAGER_TYPE.getKey(VillagerType.DESERT));
        else {
            int index = Arrays.asList(types).indexOf(BuiltInRegistries.VILLAGER_TYPE.get(visualState.getCustomTypeId())) + 1;
            if(index >= types.length) index = 0;
            visualState.setCustomTypeId(BuiltInRegistries.VILLAGER_TYPE.getKey(types[index]));
        }
    }

    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (!(target instanceof Villager villager)) return;
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;

        // attachment：直接取得資料（第一次取會建立預設 instance）
        VillagerVisualState st = villager.getData(MobCivicsAttachments.VILLAGER_VISUAL.get());

        // 在 attachment 世界裡「非 default 才送」
        if (st.isDefault()) return;

        PacketDistributor.sendToPlayer(
                sp,
                new VillagerVisualSyncS2C(villager.getId(), st.serializeNBT())
        );
    }
}
