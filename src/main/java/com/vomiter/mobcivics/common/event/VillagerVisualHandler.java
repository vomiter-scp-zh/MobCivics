package com.vomiter.mobcivics.common.event;

import com.vomiter.mobcivics.network.MobCivicsNetwork;
import com.vomiter.mobcivics.network.VillagerVisualSyncS2C;
import com.vomiter.mobcivics.common.registry.ModItems;
import com.vomiter.mobcivics.common.capabilities.visual.IVillagerVisualState;
import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualProvider;
import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;

public class VillagerVisualHandler {
    static void onInteractionTest(PlayerInteractEvent.EntityInteract event){
        if(!event.getEntity().getMainHandItem().is(ModItems.TEST_WAND.get())) return;
        if(!(event.getTarget() instanceof Villager villager)) return;
        IVillagerVisualState visualState = VillagerVisualState.getOrCreate(villager);
        visualState.setShowCustomAttireLayer(true);
        VillagerType[] types = {VillagerType.DESERT, VillagerType.JUNGLE, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.SWAMP, VillagerType.TAIGA};
        if(visualState.getCustomTypeId() == null) visualState.setCustomTypeId(BuiltInRegistries.VILLAGER_TYPE.getKey(VillagerType.DESERT));
        else {
            int index = Arrays.asList(types).indexOf(BuiltInRegistries.VILLAGER_TYPE.get(visualState.getCustomTypeId())) + 1;
            if(index >= types.length) index = 0;
            visualState.setCustomTypeId(BuiltInRegistries.VILLAGER_TYPE.getKey(types[index]));
        }
    }

    static void onAttachCaps(AttachCapabilitiesEvent<Entity> event){
        if (!(event.getObject() instanceof Villager)) return;
        VillagerVisualProvider provider = new VillagerVisualProvider();
        event.addCapability(VillagerVisualState.KEY, provider);
        // 可選：如果你想在 entity 移除時 invalidates
        event.addListener(provider::invalidate);
    }

    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (!(target instanceof Villager villager)) return;
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;

        villager.getCapability(VillagerVisualState.VILLAGER_VISUAL).ifPresent(st -> {
            MobCivicsNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> sp),
                    new VillagerVisualSyncS2C(villager.getId(), st.serializeNBT())
            );
        });
    }

}
