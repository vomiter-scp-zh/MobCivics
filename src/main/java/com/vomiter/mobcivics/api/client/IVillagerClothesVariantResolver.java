package com.vomiter.mobcivics.api.client;

import net.minecraft.world.entity.Mob;

@FunctionalInterface
public interface IVillagerClothesVariantResolver<T extends Mob> {
    /**
     * 回傳用於貼圖路徑的 variant key（例如 "husk", "drowned"）。
     * 必須是小寫且可作為資料夾名稱。
     */
    String mobcivics$getVariantKey(T entity);
}
