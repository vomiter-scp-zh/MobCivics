package com.vomiter.mobcivics.client.renderer;

import com.vomiter.mobcivics.MobCivics;
import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import com.vomiter.mobcivics.client.layer.MobCivicsWanderingTraderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class AbstractWanderingTraderHumanoidRenderer<
        T extends Mob & IVillagerDataHolder,
        M extends HumanoidModel<T>>
        extends AbstractVillagerLikeHumanoidRenderer<T, M>{
    final ResourceLocation traderTexture;

    protected AbstractWanderingTraderHumanoidRenderer(
            EntityRendererProvider.Context ctx,
            M model,
            float shadowRadius,
            ResourceLocation baseTexture,
            ResourceLocation traderTexture,
            Class<? extends Mob> entityClass) {
        super(
                ctx,
                model,
                shadowRadius,
                baseTexture,
                traderTexture.getNamespace(),
                false,
                entityClass,
                e -> ""
        );
        this.traderTexture = traderTexture;
    }

    public void addClothesLayer(){
        MobCivics.LOGGER.info("[MobCivics] Wandering Trader Model Texture:{}", traderTexture);

        this.addLayer(new MobCivicsWanderingTraderLayer<>(
                this,
                traderTexture
        ));
    }

}