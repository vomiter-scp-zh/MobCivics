package com.vomiter.mobcivics.network;

import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import com.vomiter.mobcivics.common.registry.MobCivicsAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public final class MobCivicsClientNetworkHandler implements IMobCivicsClientNetworkHandler{
    public MobCivicsClientNetworkHandler() {}

    public void handle(INetWorkPacket msg, Supplier<IPayloadContext> ctx){
        if(msg == null) return;

        else if (msg instanceof VillagerVisualSyncS2C visualS2C) {
            ctx.get().enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if(level == null) return;
                Entity e = level.getEntity(visualS2C.entityId());
                if (e instanceof Villager villager) {
                    VillagerVisualState st = villager.getData(MobCivicsAttachments.VILLAGER_VISUAL);
                    if(!st.isDefault()) st.deserializeNBT(visualS2C.tag());
                }
            });
        }
    }

}
