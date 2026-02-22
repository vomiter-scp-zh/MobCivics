package com.vomiter.mobcivics;

import net.minecraft.resources.ResourceLocation;

public class Helpers {
    public static ResourceLocation id(String namespace, String path){
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation mobCivicsId(String path){
        return id(MobCivics.MOD_ID, path);
    }

    public static ResourceLocation undeadVariantsId(String path){
        return id("undeadvariants", path);
    }
    public static ResourceLocation minecraftId(String path){
        return id("minecraft", path);
    }

    /**
    * Default: entity
     */
    public static ResourceLocation textureId(ResourceLocation rl){
        return id(
                rl.getNamespace(),
                "textures/entity/" + rl.getPath() + ".png"
        );
    }
    public static ResourceLocation textureId(ResourceLocation rl, String group){
        return id(
                rl.getNamespace(),
                "textures/" + group + "/" + rl.getPath() + ".png"
        );
    }

}
