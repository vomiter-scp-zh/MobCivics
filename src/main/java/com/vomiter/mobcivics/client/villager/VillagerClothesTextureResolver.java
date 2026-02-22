package com.vomiter.mobcivics.client.villager;

import com.vomiter.mobcivics.Helpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraftforge.registries.ForgeRegistries;

public final class VillagerClothesTextureResolver {
    private final String namespace;
    private final String variantKey;

    public VillagerClothesTextureResolver(String namespace, String variantKey) {
        this.namespace = namespace;
        this.variantKey = variantKey;
    }

    public ResourceLocation typeTexture(ResourceLocation type) {
        return Helpers.id(namespace,
                "textures/entity/villager/" + variantKey + "/type/" + textureIdPath(type) + ".png");
    }

    public ResourceLocation professionTexture(VillagerProfession profession) {
        return professionTexture(ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession));
    }

    public ResourceLocation professionTexture(ResourceLocation profession) {
        return Helpers.id(namespace,
                "textures/entity/villager/" + variantKey + "/profession/" + idPath(profession) + ".png");
    }

    public ResourceLocation levelTexture(int level) {
        return Helpers.id(namespace,
                "textures/entity/villager/" + variantKey + "/profession_level/" + level + ".png");
    }

    String textureIdPath(ResourceLocation id){
        if(id == null) return idPath(BuiltInRegistries.VILLAGER_TYPE.getKey(VillagerType.PLAINS));
        return idPath(id);
    }

    String idPath(ResourceLocation id){
        return id.getNamespace() + "/" + id.getPath();
    }

}
