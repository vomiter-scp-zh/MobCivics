package com.vomiter.mobcivics.api;

import com.vomiter.mobcivics.common.block.SkullLikeBlock;
import com.vomiter.mobcivics.common.block.WallSkullLikeBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;

import java.util.Arrays;

public class SkullHelpers {

    /**
     * VariantBlockStateBuilder b = getVariantBuilder(block);
     * var mf = models().getExistingFile(location);
     */
    public static void skullRotationStates(VariantBlockStateBuilder b, ModelFile mf) {
        for (int rot = 0; rot < 16; rot++) {
            b.partialState().with(SkullLikeBlock.ROTATION, rot)
                    .modelForState()
                    .modelFile(mf)
                    .addModel();
        }
    }

    /**
     * VariantBlockStateBuilder b = getVariantBuilder(block);
     * var mf = models().getExistingFile(location);
     */
    public static void wallSkullFacingStates(VariantBlockStateBuilder b, ModelFile mf) {

        for (var dir : Arrays.asList(
                net.minecraft.core.Direction.NORTH,
                net.minecraft.core.Direction.EAST,
                net.minecraft.core.Direction.SOUTH,
                net.minecraft.core.Direction.WEST
        )) {
            int y = ((int) dir.toYRot()) % 360; // SOUTH=0, WEST=90, NORTH=180, EAST=270

            b.partialState().with(WallSkullLikeBlock.FACING, dir)
                    .modelForState()
                    .modelFile(mf)
                    .rotationY((y + 180) % 360)
                    .addModel();
        }
    }

}
