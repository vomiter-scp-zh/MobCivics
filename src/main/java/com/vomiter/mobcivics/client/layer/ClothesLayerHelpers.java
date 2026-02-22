package com.vomiter.mobcivics.client.layer;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.MobCivics;
import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import com.vomiter.mobcivics.api.client.IVillagerWithLayers;
import com.vomiter.mobcivics.common.capabilities.visual.VillagerVisualState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClothesLayerHelpers {
    private static final Logger LOGGER = MobCivics.LOGGER;
    private static final Set<String> WARN_ONCE = ConcurrentHashMap.newKeySet();

    static ResourceLocation getTypeId(LivingEntity entity) {
        if (entity instanceof Villager villager && VillagerVisualState.has(villager)) {
            return VillagerVisualState.getOrDefault(villager).getCustomTypeId();
        } else if (entity instanceof VillagerDataHolder data) {
            // vanilla villager: 從 data.getVariant() 拿 VillagerType，再轉 key
            VillagerType type = data.getVariant();
            return BuiltInRegistries.VILLAGER_TYPE.getKey(type);
        } else if (entity instanceof IVillagerDataHolder iData) {
            VillagerType type = iData.mobcivics$getVillagerData().getType();
            return BuiltInRegistries.VILLAGER_TYPE.getKey(type);
        }
        return null;
    }


    static VillagerType getType(LivingEntity entity){
        if(entity instanceof Villager villager && VillagerVisualState.has(villager)){
            ResourceLocation customTypeId = VillagerVisualState.getOrDefault(villager).getCustomTypeId();
            if(customTypeId != null){
                if(BuiltInRegistries.VILLAGER_TYPE.get(customTypeId) != null){
                    return BuiltInRegistries.VILLAGER_TYPE.get(customTypeId);
                }
            }
        } else if (entity instanceof VillagerDataHolder data) {
            return data.getVariant();
        } else if (entity instanceof IVillagerDataHolder iData) {
            return iData.mobcivics$getVillagerData().getType();
        }
        return null;
    }

    static boolean exists(ResourceLocation id) {
        return Minecraft.getInstance().getResourceManager().getResource(id).isPresent();
    }

    static void warnOnce(String key, String fmt, Object... args) {
        //LOGGER.warn(fmt, args);
        if (WARN_ONCE.add(key)) {
            LOGGER.warn(fmt, args);
        }
    }

    // -------------------------
    // Fixed vanilla texture paths
    // -------------------------

    static boolean isZombieVillagerLike(Mob e) {
        if(e instanceof IVillagerWithLayers villager) return villager.isZombieVillagerLike();
        return e instanceof ZombieVillager; // 你的 HuskVillager 通常也會是 ZombieVillager 子類
    }

    static ResourceLocation vanillaTypeTexture(ResourceLocation typeId, boolean zombieLike) {
        String root = zombieLike ? "zombie_villager" : "villager";
        String path = (typeId == null) ? "plains" : typeId.getPath(); // 預設 fallback
        return Helpers.textureId(Helpers.minecraftId(root + "/type/" + path));
    }

    static ResourceLocation vanillaProfessionTexture(VillagerProfession profession, boolean zombieLike) {
        String root = zombieLike ? "zombie_villager" : "villager";
        return Helpers.textureId(Helpers.minecraftId(root + "/profession/" + idPath(profession)));
    }

    static ResourceLocation vanillaLevelTexture(int level, boolean zombieLike) {
        String root = zombieLike ? "zombie_villager" : "villager";
        String name = switch (level) {
            case 1 -> "stone";
            case 2 -> "iron";
            case 3 -> "gold";
            case 4 -> "emerald";
            case 5 -> "diamond";
            default -> "stone"; // 或直接 return null / ItemStack.EMPTY 概念：不畫
        };
        return Helpers.textureId(Helpers.minecraftId(root + "/profession_level/" + name));
    }

    private static String idPath(VillagerType type) {
        var key = BuiltInRegistries.VILLAGER_TYPE.getKey(type);
        return key == null ? "plains" : key.getPath();
    }

    private static String idPath(VillagerProfession prof) {
        var key = ForgeRegistries.VILLAGER_PROFESSIONS.getKey(prof);
        return key == null ? "none" : key.getPath();
    }

}
