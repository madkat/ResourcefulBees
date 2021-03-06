package com.resourcefulbees.resourcefulbees.container;

import com.resourcefulbees.resourcefulbees.mixin.ContainerAccessor;
import com.resourcefulbees.resourcefulbees.registry.ModContainers;
import com.resourcefulbees.resourcefulbees.tileentity.HoneyGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public class HoneyGeneratorContainer extends Container {

    public HoneyGeneratorTileEntity honeyGeneratorTileEntity;
    public PlayerEntity player;

    public HoneyGeneratorContainer(int id, World world, BlockPos pos, PlayerInventory inv) {
        super(ModContainers.HONEY_GENERATOR_CONTAINER.get(), id);

        this.player = inv.player;
        honeyGeneratorTileEntity = (HoneyGeneratorTileEntity) world.getTileEntity(pos);

        if (honeyGeneratorTileEntity != null) {
            this.addSlot(new SlotItemHandlerUnconditioned(honeyGeneratorTileEntity.h, HoneyGeneratorTileEntity.HONEY_BOTTLE_INPUT, 36, 20) {
                public boolean isItemValid(ItemStack stack) {
                    return honeyGeneratorTileEntity.h.isItemValid(HoneyGeneratorTileEntity.HONEY_BOTTLE_INPUT, stack);
                }
            });
            this.addSlot(new OutputSlot(honeyGeneratorTileEntity.h, HoneyGeneratorTileEntity.BOTTLE_OUTPUT, 36, 58));

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                }
            }

            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
            }
        }
    }

    public int getEnergy() { return honeyGeneratorTileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0); }
    
    public int getFluid() { return honeyGeneratorTileEntity.fluidTank.getFluidAmount(); }
    
    public int getMaxEnergy() { return honeyGeneratorTileEntity.energyStorage.getMaxEnergyStored(); }

    public int getMaxFluid() { return honeyGeneratorTileEntity.fluidTank.getCapacity(); }

    public int getTime() { return honeyGeneratorTileEntity.fluidFilled; }

    public int getEnergyTime() { return honeyGeneratorTileEntity.energyFilled; }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index <= 1) {
                if (!this.mergeItemStack(itemstack1, 2, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (honeyGeneratorTileEntity == null) {
            return;
        }

        for (IContainerListener listener : ((ContainerAccessor) this).getListeners()) {
            honeyGeneratorTileEntity.sendGUINetworkPacket(listener);
        }
    }
}
