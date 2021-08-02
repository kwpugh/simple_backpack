package com.kwpugh.simple_backpack.backpack;

import com.kwpugh.simple_backpack.Backpack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class BackpackScreenHandler extends ScreenHandler
{
    private final Inventory inventory;
    private final PlayerInventory playerInventory;
    public final int inventoryWidth;
    public final int inventoryHeight;
    public final String customTitle;

    //private ItemStack backpack;

    public BackpackScreenHandler(final int syncId, final PlayerInventory playerInventory, final Inventory inventory, final int inventoryWidth, final int inventoryHeight, final Hand hand, String customTitle)
    {
        super(null, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        this.customTitle = customTitle;

        checkSize(inventory, inventoryWidth * inventoryHeight);
        inventory.onOpen(playerInventory.player);
        setupSlots(false);
    }

    public class BackpackLockedSlot extends Slot
    {
        public BackpackLockedSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return stackMovementIsAllowed(getStack());
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return stackMovementIsAllowed(stack);
        }

        public boolean stackMovementIsAllowed(ItemStack stack) {
            return !(stack.getItem() instanceof BackpackItem);
        }
    }

    @Override
    public void close(final PlayerEntity player)
    {
        super.close(player);
        inventory.onClose(player);
    }

    public void setupSlots(final boolean includeChestInventory)
    {
        int i = (this.inventoryHeight - 4) * 18;

        int n;
        int m;
        for(n = 0; n < this.inventoryHeight; ++n)
        {
           for(m = 0; m < 9; ++m)
           {
              this.addSlot(new BackpackLockedSlot(inventory, m + n * 9, 8 + m * 18, 18 + n * 18));
           }
        }

        for(n = 0; n < 3; ++n)
        {
           for(m = 0; m < 9; ++m)
           {
              this.addSlot(new BackpackLockedSlot(playerInventory, m + n * 9 + 9, 8 + m * 18, 103 + n * 18 + i));
           }
        }

        for(n = 0; n < 9; ++n)
        {
           this.addSlot(new BackpackLockedSlot(playerInventory, n, 8 + n * 18, 161 + i));
        }
    }

    @Override
    public boolean canUse(final PlayerEntity player)
    {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(final PlayerEntity player, final int invSlot)
    {
        ItemStack newStack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(invSlot);
        final ItemStack originalStack = slot.getStack();
        Item testItem = originalStack.getItem();

        if(testItem != Backpack.BACKPACK)
        {
        	 if (slot != null && slot.hasStack())
             {
     	        newStack = originalStack.copy();
     	        if (invSlot < this.inventory.size())
     	        {
     	            if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true))
     	            {
     	                return ItemStack.EMPTY;
     	            }
     	        }
     	        else if (!this.insertItem(originalStack, 0, this.inventory.size(), false))
     	        {
     	            return ItemStack.EMPTY;
     	        }

     	        if (originalStack.isEmpty())
     	        {
     	            slot.setStack(ItemStack.EMPTY);
     	        }
     	        else
     	        {
     	            slot.markDirty();
     	        }
             }

             return newStack;
        }

        return newStack;
    }
}
