package com.vomiter.mobcivics.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ModDataGenerator {
    public static void generateData(GatherDataEvent event){
        System.out.println("[mobcivics] codeSource=" +
                ModDataGenerator.class.getProtectionDomain().getCodeSource().getLocation());
        System.out.println("[mobcivics] modContainer=" + event.getModContainer().getModId());

        if (!"mobcivics".equals(event.getModContainer().getModId())) {
            return;
        }

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        new ModTagProviders(event);
    }
}
