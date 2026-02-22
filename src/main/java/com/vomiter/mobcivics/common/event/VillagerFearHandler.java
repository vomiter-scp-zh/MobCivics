package com.vomiter.mobcivics.common.event;

import com.vomiter.mobcivics.api.common.entity.IVillagerThreat;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class VillagerFearHandler {

    /**
     * per-level 的「目前快取對應的 gameTime」
     * tick 變了就清空所有 per-tick 快取。
     */
    private static final Map<ServerLevel, Long> LAST_TICK = new WeakHashMap<>();

    /**
     * per-level、per-tick：villagerId -> best threat entityId
     */
    private static final Map<ServerLevel, Int2IntOpenHashMap> BEST_THREAT_ID = new WeakHashMap<>();

    /**
     * per-level、per-tick：villagerId -> best distance squared
     */
    private static final Map<ServerLevel, Int2DoubleOpenHashMap> BEST_DIST_SQR = new WeakHashMap<>();

    private static void beginTick(ServerLevel level, long now) {
        Long last = LAST_TICK.get(level);
        if (last != null && last == now) return;

        LAST_TICK.put(level, now);

        Int2IntOpenHashMap bestId = BEST_THREAT_ID.get(level);
        if (bestId == null) {
            bestId = new Int2IntOpenHashMap();
            // default return 不重要，只要 containsKey 判斷就好
            BEST_THREAT_ID.put(level, bestId);
        } else {
            bestId.clear();
        }

        Int2DoubleOpenHashMap bestDist = BEST_DIST_SQR.get(level);
        if (bestDist == null) {
            bestDist = new Int2DoubleOpenHashMap();
            BEST_DIST_SQR.put(level, bestDist);
        } else {
            bestDist.clear();
        }
    }

    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof IVillagerThreat threat)) return;
        if (!threat.villagerFearEnabled(entity)) return;
        if (!(entity.level() instanceof ServerLevel level)) return;

        int scanInterval = Math.max(1, threat.villagerFearScanIntervalTicks());
        if ((entity.tickCount % scanInterval) != 0) return;

        double radius = Math.max(0.0D, threat.villagerFearRadius());
        if (radius <= 0.0D) return;

        int ttl = Math.max(1, threat.villagerFearMemoryTtlTicks());
        boolean requireLos = threat.villagerFearRequiresLineOfSight();

        long now = level.getGameTime();
        beginTick(level, now);

        Int2IntOpenHashMap bestThreatId = BEST_THREAT_ID.get(level);
        Int2DoubleOpenHashMap bestDistSqr = BEST_DIST_SQR.get(level);
        if (bestThreatId == null || bestDistSqr == null) return; // 理論上不會發生

        AABB box = entity.getBoundingBox().inflate(radius, 4.0D, radius);
        double radiusSqr = radius * radius;

        for (Villager villager : level.getEntitiesOfClass(Villager.class, box)) {
            double d2 = villager.distanceToSqr(entity);
            if (d2 > radiusSqr) continue;
            if (requireLos && !villager.getSensing().hasLineOfSight(entity)) continue;

            int vid = villager.getId();

            // (1) 如果這個 villager 在本 tick 還沒被處理過：
            //     先把「既有 NEAREST_HOSTILE 是否更近」這個檢查做一次
            if (!bestThreatId.containsKey(vid)) {
                Brain<Villager> brain = villager.getBrain();
                var currentOpt = brain.getMemory(MemoryModuleType.NEAREST_HOSTILE);
                if (currentOpt.isPresent()) {
                    LivingEntity current = currentOpt.get();
                    if (current.isAlive()) {
                        double curD2 = villager.distanceToSqr(current);
                        // 既有 hostile 更近 => 直接跳過，不建立本 tick 的候選
                        if (curD2 <= d2) {
                            continue;
                        }
                    }
                }

                // 既有 hostile 不存在或更遠 => 用目前 threat 初始化本 tick 最佳候選
                bestThreatId.put(vid, entity.getId());
                bestDistSqr.put(vid, d2);

                // 直接寫入
                brain.setMemoryWithExpiry(MemoryModuleType.NEAREST_HOSTILE, entity, ttl);
                continue;
            }

            // (2) 本 tick 已有候選：只在更近時覆蓋
            double bestD2 = bestDistSqr.get(vid);
            if (d2 >= bestD2) continue;

            // 更近 => 更新快取 + 覆蓋 memory
            bestThreatId.put(vid, entity.getId());
            bestDistSqr.put(vid, d2);

            villager.getBrain().setMemoryWithExpiry(MemoryModuleType.NEAREST_HOSTILE, entity, ttl);
        }
    }
}
