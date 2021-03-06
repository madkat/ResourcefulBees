package com.resourcefulbees.resourcefulbees.block.multiblocks.centrifuge;

import com.resourcefulbees.resourcefulbees.registry.ModTileEntityTypes;
import com.resourcefulbees.resourcefulbees.tileentity.multiblocks.centrifuge.CentrifugeControllerTileEntity;
import com.resourcefulbees.resourcefulbees.tileentity.multiblocks.centrifuge.EliteCentrifugeCasingTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EliteCentrifugeCasingBlock extends CentrifugeCasingBlock {
    public EliteCentrifugeCasingBlock(Properties properties) { super(properties); }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EliteCentrifugeCasingTileEntity(ModTileEntityTypes.ELITE_CENTRIFUGE_CASING_ENTITY.get());
    }

    @Override
    protected CentrifugeControllerTileEntity getControllerEntity(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof EliteCentrifugeCasingTileEntity) {
            return ((EliteCentrifugeCasingTileEntity) tileEntity).getController();
        }
        return null;
    }
}
