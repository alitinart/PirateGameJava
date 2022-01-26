/*
*
* Item Manager
*
* Version 1.0
*
* Date 1/26/2022
*
* */

package com.alpha.pirategame.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack Cannon;
    public static ItemStack Flintknock;
    public static ItemStack Bullet;
    public static ItemStack HealingPotion;
    public static ItemStack Coin;
    public static ItemStack ShopSpawner;
    public static ItemStack PirateSword;

    public static void init() {
        /* Initialize all Custom Items */
        createCannon();
        createFlintknock();
        createBullet();
        createBullet();
        createHealingPotion();
        createCoin();
        createShopSpawner();
        createSword();
    }

    private static void createCannon() {
        ItemStack item = new ItemStack(Material.OAK_BOAT, 1);

        /* Setup Meta */
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Cannon");
        List<String> lore = new ArrayList<String>();
        lore.add("§7Drop while standing on top");
        lore.add("§7of a §6Gold Block");
        meta.setLore(lore);

        /* Set Meta */
        item.setItemMeta(meta);

        /* Initialize Item */
        Cannon = item;
    }

    private static void createFlintknock() {
        ItemStack item =  new ItemStack(Material.BOW, 1);

        /* Setup Meta */
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Flintknock");
        List<String> lore = new ArrayList<String>();
        lore.add("Shooting this might send you back...");
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.setCustomModelData(1);

        /* Set Meta */
        item.setItemMeta(meta);

        /* Initialize Item */
        Flintknock = item;
    }

    private static void createBullet() {
        ItemStack item = new ItemStack(Material.ARROW, 1);

        /* Setup Meta */
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cFlintknock Bullet");
        meta.setCustomModelData(5);

        /* Set Meta */
        item.setItemMeta(meta);

        /* Initialize Item */
        Bullet = item;
    }

    private static void createHealingPotion() {
        ItemStack item =  new ItemStack(Material.SPLASH_POTION, 1);

        /* Setup Meta */
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2), false);
        meta.setDisplayName("§6Healing Potion");
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        /* Set Meta */
        item.setItemMeta(meta);

        /* Initialize Item */
        HealingPotion = item;
    }

    private static void createCoin() {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);

        /* Setup Meta */
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§7Buy items in the shop.");
        meta.setLore(lore);
        meta.setDisplayName("§cV-Coin");

        /* Set Meta */
        item.setItemMeta(meta);

        /* Initialize Item */
        Coin = item;
    }

    private static void createShopSpawner() {
        ItemStack item = new ItemStack(Material.VILLAGER_SPAWN_EGG, 1);

        /* Setup Meta */
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§7Spawn Shop");
        meta.setLore(lore);
        meta.setDisplayName("§cPirate Shop");

        /* Set Meta */
        item.setItemMeta(meta);

        /* Initialize Item */
        ShopSpawner = item;
    }

    private static void createSword() {
        ItemStack item = new ItemStack(Material.IRON_SWORD, 1);

        /* Setup Meta */
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§7CAUTION: Very Sharp");
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.setDisplayName("§cPirate Sword");
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);

        /* Set Meta */
        item.setItemMeta(meta);

        /* Initialize Item */
        PirateSword = item;
    }
}
