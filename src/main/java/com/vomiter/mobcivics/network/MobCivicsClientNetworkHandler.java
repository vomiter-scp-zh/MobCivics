package com.vomiter.mobcivics.network;

import com.vomiter.mobcivics.common.capabilities.visual.DefaultVillagerVisualState;
import com.vomiter.mobcivics.common.capabilities.visual.IVillagerVisualState;
import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class MobCivicsClientNetworkHandler implements IMobCivicsClientNetworkHandler{
    public MobCivicsClientNetworkHandler() {}

    public void handle(INetWorkPacket msg, Supplier<NetworkEvent.Context> ctx){
        if(msg == null) return;

        else if (msg instanceof VillagerVisualSyncS2C visualS2C) {
            ctx.get().enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if(level == null) return;
                Entity e = level.getEntity(visualS2C.entityId());
                if (e instanceof Villager villager) {
                    IVillagerVisualState st = VillagerVisualState.getOrDefault(villager);
                    if(!(st instanceof DefaultVillagerVisualState)) st.deserializeNBT(visualS2C.tag());
                }
            });
            ctx.get().setPacketHandled(true);

        }
    }

}
