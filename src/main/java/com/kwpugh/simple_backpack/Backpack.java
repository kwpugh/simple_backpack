package com.kwpugh.simple_backpack;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kwpugh.simple_backpack.backpack.BackpackInventoryInterface;
import com.kwpugh.simple_backpack.backpack.BackpackItem;
import com.kwpugh.simple_backpack.backpack.BackpackScreenHandler;
import com.kwpugh.simple_backpack.enderpack.EnderPackItem;
import com.kwpugh.simple_backpack.voidpack.VoidPackInventoryInterface;
import com.kwpugh.simple_backpack.voidpack.VoidPackItem;
import com.kwpugh.simple_backpack.voidpack.VoidPackScreenHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class Backpack implements ModInitializer
{
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "simple_backpack";
    public static final String MOD_NAME = "SimpleBackpack";
    public static final ItemGroup SIMPLE_BACKPACK_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "simple_backpack_group"), () -> new ItemStack(Backpack.BACKPACK));

    public static final Identifier BACKPACK_IDENTIFIER = new Identifier(MOD_ID, "backpack");
    public static final Identifier VOID_PACK_IDENTIFIER = new Identifier(MOD_ID, "void_pack");
    public static final Identifier ENDER_PACK_IDENTIFIER = new Identifier(MOD_ID, "ender_pack");

    public static final String BACKPACK_TRANSLATION_KEY = Util.createTranslationKey("container", BACKPACK_IDENTIFIER);
    public static final String VOID_PACK_TRANSLATION_KEY = Util.createTranslationKey("container", VOID_PACK_IDENTIFIER);
    public static final String ENDER_PACK_TRANSLATION_KEY = Util.createTranslationKey("container", ENDER_PACK_IDENTIFIER);

    public static final Item BACKPACK = new BackpackItem(new Item.Settings().group(Backpack.SIMPLE_BACKPACK_GROUP).maxCount(1));
    public static final Item VOID_PACK = new VoidPackItem(new Item.Settings().group(Backpack.SIMPLE_BACKPACK_GROUP).maxCount(1));
    public static final Item ENDER_PACK = new EnderPackItem(new Item.Settings().group(Backpack.SIMPLE_BACKPACK_GROUP).maxCount(1));

    @Override
    public void onInitialize()
    {
        ContainerProviderRegistry.INSTANCE.registerFactory(BACKPACK_IDENTIFIER, ((syncId, identifier, player, buf) -> {
            final ItemStack stack = buf.readItemStack();
            final Hand hand = buf.readInt() == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
            final BackpackInventoryInterface inventory = BackpackItem.getInventory(stack, hand, player);
            final String customTitle = buf.readString();

            return new BackpackScreenHandler(syncId, player.getInventory(), inventory.getInventory(), inventory.getInventoryWidth(), inventory.getInventoryHeight(), hand, customTitle);
        }));

        ContainerProviderRegistry.INSTANCE.registerFactory(VOID_PACK_IDENTIFIER, ((syncId, identifier, player, buf) -> {
            final ItemStack stack = buf.readItemStack();
            final Hand hand = buf.readInt() == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
            final VoidPackInventoryInterface inventory = VoidPackItem.getInventory(stack, hand, player);

            return new VoidPackScreenHandler(syncId, player.getInventory(), inventory.getInventory(), inventory.getInventoryWidth(), inventory.getInventoryHeight(), hand);
        }));

        Registry.register(Registry.ITEM, BACKPACK_IDENTIFIER, BACKPACK);
        Registry.register(Registry.ITEM, VOID_PACK_IDENTIFIER, VOID_PACK);
        Registry.register(Registry.ITEM, ENDER_PACK_IDENTIFIER, ENDER_PACK);
    }

    public static void log(Level level, String message)
    {
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }
}