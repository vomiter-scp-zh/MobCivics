package com.vomiter.mobcivics.client.renderer;

import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import com.vomiter.mobcivics.client.layer.MobCivicsVillagerClothesLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ZombieVillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.jetbrains.annotations.NotNull;

/**
 * 給「類村民 humanoid」用的共用 renderer：
 * - 不繼承 ZombieVillagerRenderer（避免原版 VillagerProfessionLayer）
 * - 內建 villager clothes/profession layer（可指定 namespace）
 * - 裝甲 layer 預設僅在 entityClass 是 ZombieVillager 系時啟用（可覆寫）
 *
 * 需求：
 * - Entity 必須能提供 VillagerData（透過 IVillagerDataHolder）
 */
public abstract class AbstractVillagerLikeHumanoidRenderer<
        T extends Mob & IVillagerDataHolder,
        M extends HumanoidModel<T>
        > extends HumanoidMobRenderer<T, M> {

    private final ResourceLocation baseTexture;

    protected AbstractVillagerLikeHumanoidRenderer(
            EntityRendererProvider.Context ctx,
            M model,
            float shadowRadius,
            ResourceLocation baseTexture,
            String clothesTextureNamespace,
            boolean renderLevelBadge,
            Class<? extends Mob> entityClass,
            com.vomiter.mobcivics.api.client.IVillagerClothesVariantResolver<T> variantResolver
    ) {
        super(ctx, model, shadowRadius);
        this.baseTexture = baseTexture;

        // 1) 裝甲 layer：預設僅 ZombieVillager 系啟用（避免 skeletal / 其他 humanoid 被硬套 villager armor）
        maybeAddArmorLayer(ctx, entityClass);

        // 2) 你的衣服/職業 layer：吃指定 namespace 的 assets
        this.addLayer(new MobCivicsVillagerClothesLayer<>(
                 this,
                clothesTextureNamespace,
                renderLevelBadge,
                variantResolver
        ));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return baseTexture;
    }

    /**
     * Armor gating 的入口：預設只讓 ZombieVillager (含其子類) 有裝甲 layer。
     * 子類若要強制開/關，可覆寫此方法。
     */
    protected void maybeAddArmorLayer(EntityRendererProvider.Context ctx, Class<? extends Mob> entityClass) {
        if (!ZombieVillager.class.isAssignableFrom(entityClass)) return;
        this.addLayer(createZombieVillagerArmorLayer(ctx));
    }

    /**
     * 預設的 ZombieVillager 裝甲 layer。子類若要換成不同 LayerLocation/Model，可覆寫。
     *
     * 注意：這裡刻意回傳 raw type，避免 HumanoidArmorLayer 的泛型在 1.20.1 被編譯器/IDE 卡死。
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected HumanoidArmorLayer createZombieVillagerArmorLayer(EntityRendererProvider.Context ctx) {
        ZombieVillagerModel inner = new ZombieVillagerModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_INNER_ARMOR));
        ZombieVillagerModel outer = new ZombieVillagerModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_OUTER_ARMOR));
        return new HumanoidArmorLayer(this, inner, outer, ctx.getModelManager());
    }
}
