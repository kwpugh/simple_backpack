package com.kwpugh.simple_backpack.voidpack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class VoidpackItem extends Item
{
    public VoidpackItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canBeNested()
    {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        if(player.getWorld().isClient) return TypedActionResult.pass(player.getStackInHand(hand));

        ItemStack stack = player.getStackInHand(hand);
        
        player.openHandledScreen(createScreenHandlerFactory(stack));

        return TypedActionResult.success(stack);
    }

    private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack)
    {
//        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
//                new VoidpackScreenHandler(syncId, inventory, new VoidpackInventory(stack)), stack.getName());

        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
                NewVoidpackScreenHandler.createGeneric9x6(i, playerInventory, new VoidpackInventory(stack)), stack.getName());

    }
}