/*
 *
 * Shop GUI
 *
 * Version 1.0
 *
 * Date 1/26/2022
 *
 * */

package com.alpha.pirategame.guis;

import com.alpha.pirategame.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class shopGUI implements InventoryHolder {

    private Inventory inv;

    public shopGUI () {
        inv = Bukkit.createInventory(this, 54, "Pirate Shop");
        init();
    }

    private void init() {
        ItemStack item = null;
        ItemStack Border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);

        for(int i = 0; i < 54; i++) {
            item = createItem("§bBorder", Border, Collections.singletonList("§7Just a border"), 1, 0);
            inv.setItem(i, item);
        }

        // Items

        item = createItem("§6Cannon", ItemManager.Cannon, Collections.singletonList("§7§bPrice: 12 V-Coins"), 1, 0);
        inv.setItem(10, item);

        item = createItem("§6Flintknock", ItemManager.Flintknock, Collections.singletonList("§7§bPrice: 6 V-Coins"), 1, 1);
        inv.setItem(11, item);

        item = createItem("§6Pirate Sword", ItemManager.PirateSword, Collections.singletonList("§7§bPrice: 3 V-Coins"), 1, 0);
        inv.setItem(12, item);

        item = createItem("§6Flintknock Bullet's", ItemManager.Bullet, Collections.singletonList("§7§bPrice: 2 V-Coins"), 32, 5);
        inv.setItem(13, item);

        item = createItem("§6Healing Potion", ItemManager.HealingPotion, Collections.singletonList("§7§bPrice: 3 V-Coins"), 1, 0);
        inv.setItem(14, item);



    }

    private ItemStack createItem(String name, ItemStack stack, List<String> lore, int amount, int customModelData) {
        ItemStack item = new ItemStack(stack.getType(), amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(lore);
        meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}