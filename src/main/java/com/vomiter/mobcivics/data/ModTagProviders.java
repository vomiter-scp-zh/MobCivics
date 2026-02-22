package com.vomiter.mobcivics.data;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.MobCivics;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModTagProviders {
    DataGenerator generator;
    PackOutput output;
    CompletableFuture<HolderLookup.Provider> lookupProvider;
    ExistingFileHelper helper;

    public ModTagProviders(GatherDataEvent event){
        generator = event.getGenerator();
        output = generator.getPackOutput();
        lookupProvider = event.getLookupProvider();
        helper = event.getExistingFileHelper();

        var blockTags = new BlockTags();
        var itemTags = new ItemTags(blockTags);
        var entityTags = new EntityTags();
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), itemTags);
        generator.addProvider(event.includeClient(), entityTags);

    }

    interface TagUtil<T> {
        ResourceKey<Registry<T>> key();

        default TagKey<T> create(ResourceLocation id){
            return TagKey.create(
                    key(),
                    id
            );
        }

    }

    public class EntityTags extends EntityTypeTagsProvider implements TagUtil<EntityType<?>>{
        public EntityTags() {
            super(output, lookupProvider, MobCivics.MOD_ID, helper);
        }
        public TagKey<EntityType<?>> ZOMBIES_THAT_PIGLINS_FEAR = create(Helpers.mobCivicsId("zombies_that_piglins_fear"));

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {
            tag(ZOMBIES_THAT_PIGLINS_FEAR)
                    .addOptional(Helpers.undeadVariantsId("drowned_piglin"))
                    .addOptional(Helpers.undeadVariantsId("drowning_zombified_piglin"))
            ;
        }

        @Override
        public ResourceKey<Registry<EntityType<?>>> key() {
            return Registries.ENTITY_TYPE;
        }
    }


    class BlockTags extends BlockTagsProvider {

        public BlockTags() {
            super(output, lookupProvider, MobCivics.MOD_ID, helper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {

        }
    }

    class ItemTags extends ItemTagsProvider {
        public ItemTags(BlockTags blockTags) {
            super(output, lookupProvider, blockTags.contentsGetter(), MobCivics.MOD_ID, helper);
        }
        static TagKey<Item> create(ResourceLocation id){
            return TagKey.create(
                    Registries.ITEM,
                    id
            );
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
            //tag(test).add(Items.ACACIA_BOAT);
        }
    }


}
