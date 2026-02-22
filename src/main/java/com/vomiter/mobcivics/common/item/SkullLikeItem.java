package com.vomiter.mobcivics.common.item;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class SkullLikeItem extends StandingAndWallBlockItem {
    public SkullLikeItem(Block head, Block wallHead, Properties properties, Direction direction) {
        super(head, wallHead, properties, direction);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(@NotNull ItemStack stack) {
        return EquipmentSlot.HEAD;
    }


}
