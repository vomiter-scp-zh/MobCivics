package com.vomiter.mobcivics.common.block;

import com.mojang.serialization.MapCodec;
import com.vomiter.mobcivics.MobCivics;
import com.vomiter.mobcivics.common.block.blockentity.SkullLikeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WallSkullLikeBlock extends AbstractSkullBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public WallSkullLikeBlock(Properties props) {
        super(props);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SkullLikeBlockEntity(pos, state);
    }

    private static final VoxelShape NORTH = Block.box(4, 4, 8, 12, 12, 16);
    private static final VoxelShape SOUTH = Block.box(4, 4, 0, 12, 12, 8);
    private static final VoxelShape WEST  = Block.box(8, 4, 4, 16, 12, 12);
    private static final VoxelShape EAST  = Block.box(0, 4, 4, 8, 12, 12);

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return shapeByFacing(state);
    }

    private static boolean DEBUG_ONCE = true;
    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        VoxelShape s = shapeByFacing(state);

        if (DEBUG_ONCE) {
            DEBUG_ONCE = false;

            var src = this.getClass().getProtectionDomain().getCodeSource();
            MobCivics.LOGGER.info("[WallSkullLikeBlock] class=" + this.getClass().getName()
                    + " from=" + (src == null ? "null" : src.getLocation()));

            // 印出 NORTH 常數的實際 boxes（驗證你編譯進去的是 8..16 還是 12..16）
            NORTH.forAllBoxes((x1,y1,z1,x2,y2,z2) ->
                    MobCivics.LOGGER.info("[WallSkullLikeBlock] NORTH const box=" + x1+","+y1+","+z1+" -> "+x2+","+y2+","+z2));
        }

        return s;
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                   @NotNull BlockPos pos) {
        return shapeByFacing(state);
    }

    private static VoxelShape shapeByFacing(BlockState state) {
        Direction dir = state.getValue(WallSkullLikeBlock.FACING); // 下面第2部分會加這個 property
        return switch (dir) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }


}
