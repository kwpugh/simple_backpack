package com.kwpugh.simple_backpack.voidpack;

import com.google.common.collect.Sets;
import com.kwpugh.simple_backpack.backpack.BackpackItem;
import com.kwpugh.simple_backpack.bundle.SimpleBundleItem;
import com.kwpugh.simple_backpack.bundle.VoidBundleItem;
import com.kwpugh.simple_backpack.enderpack.EnderPackItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Set;

public class NewVoidpackScreenHandler extends ScreenHandler
{
    private static final int field_30780 = 9;
    private final Inventory inventory;
    private final int rows;
    public static final Set<Item> SHULKER_BOXES;

    private NewVoidpackScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, int rows) {
        this(type, syncId, playerInventory, new SimpleInventory(9 * rows), rows);
    }

    public static NewVoidpackScreenHandler createGeneric9x6(int syncId, PlayerInventory playerInventory) {
        return new NewVoidpackScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, 6);
    }

    public static NewVoidpackScreenHandler createGeneric9x6(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        return new NewVoidpackScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, inventory, 6);
    }

    public NewVoidpackScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
        super(type, syncId);
        checkSize(inventory, rows * 9);
        this.inventory = inventory;
        this.rows = rows;
        inventory.onOpen(playerInventory.player);
        int i = (this.rows - 4) * 18;

        int j;
        int k;
        for(j = 0; j < this.rows; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlot(new VoidpackSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(j = 0; j < 3; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlot(new VoidpackSlot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for(j = 0; j < 9; ++j) {
            this.addSlot(new VoidpackSlot(playerInventory, j, 8 + j * 18, 161 + i));
        }

    }

    public static class VoidpackSlot extends Slot
    {
        public VoidpackSlot(Inventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity)
        {
            return canMoveStack(getStack());
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return canMoveStack(stack);
        }

        // Prevents items that override canBeNested() from being inserted into backpack
        public boolean canMoveStack(ItemStack stack)
        {
            return stack.getItem().canBeNested();
        }
    }

    @Override
    public void onSlotClick(int slotId, int clickData, SlotActionType actionType, PlayerEntity playerEntity)
    {
        // Prevents single or shift-click while pack is open
        if (slotId >= 0)  // slotId < 0 are used for networking internals
        {
            ItemStack stack = getSlot(slotId).getStack();

            if(stack.getItem() instanceof BackpackItem ||
                    stack.getItem() instanceof EnderPackItem ||
                    stack.getItem() instanceof SimpleBundleItem ||
                    SHULKER_BOXES.contains(stack.getItem()))
            {
                // Return to caller with no action
                return;
            }

        }

        super.onSlotClick(slotId, clickData, actionType, playerEntity);
    }
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);

        ItemStack originalStack = slot.getStack();
        Item testItem = originalStack.getItem();

        if(testItem instanceof BackpackItem ||
                testItem instanceof SimpleBundleItem ||
                testItem instanceof VoidBundleItem ||
                SHULKER_BOXES.contains(testItem))
        {
            return ItemStack.EMPTY;
        }

        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < this.rows * 9) {
                if (!this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }

    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getRows() {
        return this.rows;
    }

    static
    {
        SHULKER_BOXES = Sets.newHashSet(Items.SHULKER_BOX, Items.BLACK_SHULKER_BOX, Items.BLUE_SHULKER_BOX,
                Items.BROWN_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.GREEN_SHULKER_BOX,
                Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX,
                Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.RED_SHULKER_BOX,
                Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX, Items.PURPLE_SHULKER_BOX);
    }
}