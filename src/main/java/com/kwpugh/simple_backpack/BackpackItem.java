package com.kwpugh.simple_backpack;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BackpackItem extends Item {
    public BackpackItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if(!world.isClient)
        {
            ContainerProviderRegistry.INSTANCE.openContainer(Backpack.BACKPACK_IDENTIFIER, user, buf -> {
                buf.writeItemStack(user.getStackInHand(hand));
                buf.writeInt(hand == Hand.MAIN_HAND ? 0 : 1);
            });
        }

        return super.use(world, user, hand);
    }

    public static BackpackInventory getInventory(ItemStack stack, Hand hand, PlayerEntity player)
    {
        if(!stack.hasTag())
        {
            stack.setTag(new CompoundTag());
        }

        if(!stack.getTag().contains("backpack"))
        {
            stack.getTag().put("backpack", new CompoundTag());
        }

        return new BackpackInventory(stack.getTag().getCompound("backpack"), hand, player);
    }
}