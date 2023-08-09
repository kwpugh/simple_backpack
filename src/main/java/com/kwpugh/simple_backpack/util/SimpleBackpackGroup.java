package com.kwpugh.simple_backpack.util;

import com.kwpugh.simple_backpack.Backpack;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SimpleBackpackGroup
{
    public static void addGroup()
    {
        // force class run when we want
    }

    private static final ItemGroup SIMPLE_BACKUP_GROUP = FabricItemGroup.builder(new Identifier(Backpack.MOD_ID, "simple_backpack_group"))
            .icon(() -> new ItemStack(Backpack.BACKPACK))
            .entries((enabledFeatures, entries) -> {
                entries.add(Backpack.BACKPACK);
                entries.add(Backpack.VOID_PACK);
                entries.add(Backpack.ENDER_PACK);
                entries.add(Backpack.SIMPLE_BUNDLE);
                entries.add(Backpack.VOID_BUNDLE);
                entries.add(Backpack.PORTABLE_CRAFTER);
            })
            .build();
}