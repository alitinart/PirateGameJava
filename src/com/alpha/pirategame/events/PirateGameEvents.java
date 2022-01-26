/*
*
* Events ( PirateGameEvents )
*
* Version 1.0
*
* Date 1/26/2022
*
* */

package com.alpha.pirategame.events;

import com.alpha.pirategame.guis.shopGUI;
import com.alpha.pirategame.items.ItemManager;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PirateGameEvents implements Listener {
    // Initialize both hashmaps ( will be used later for cooldown and vehicle verification )
    Map<String, Boolean> firstTimeInCannon = new HashMap<String, Boolean>();
    Map<String, Long> cooldowns = new HashMap<String, Long>();

    // Create a ArrayList to later check if the player is sitting on a cannon
    ArrayList<String> cannonCheck = new ArrayList<>();

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item droppedItem = (Item) event.getItemDrop();

        // Get Items
        ItemStack Cannon = ItemManager.Cannon;

        // Check if the item dropped is a Cannon
        if(droppedItem.getItemStack().getItemMeta().equals(Cannon.getItemMeta())) {
            // If it is cannon check if player was standing on Gold Block
            if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GOLD_BLOCK) {
                // Tell Player that the Cannon has been dropped
                player.sendMessage(ChatColor.GOLD + "Dropped Cannon");

                // Remove Dropped Cannon
                droppedItem.remove();

                // Init Cannon Check
                cannonCheck.add("Cannon");

                // Spawn Boat where the player is
                Boat boat = (Boat) player.getWorld().spawnEntity(player.getLocation(), EntityType.BOAT);
                // Set boat properties
                    // Add Scoreboard Tag to later check for interaction
                    boat.addScoreboardTag("Cannon");
                boat.setGravity(false);

                // Create a AreaEffectCloud and add the boat as a passager so the player can't move the boat.
                AreaEffectCloud areaEffectCloud = (AreaEffectCloud) player.getWorld().spawnEntity(player.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                areaEffectCloud.setDuration(-1);
                areaEffectCloud.setWaitTime(-2147483648);
                areaEffectCloud.setParticle(Particle.ASH, false);
                areaEffectCloud.addPassenger(boat);
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        // Get the shop GUI
        shopGUI gui = new shopGUI();
        // Check if the entity name is Pirate Shop with red text
        if(event.getRightClicked().getName().equals("§cPirate Shop")) {
            // Open shop GUI
            event.setCancelled(true);
            event.getPlayer().openInventory(gui.getInventory());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        // Check if block is Gold Ore
        if(event.getBlock().getType().equals(Material.GOLD_ORE)){
            // Create the ItemStack to give to player
            ItemStack vCoins = new ItemStack(ItemManager.Coin.getType(), 3);
            vCoins.setItemMeta(ItemManager.Coin.getItemMeta());
            // If it give the player 3 V-Coins and cancle item drop
            event.setDropItems(false);
            player.getInventory().addItem(vCoins);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if player was sitting on a vehicle when the the inteaction was done
        if (player.isInsideVehicle()) {
            // Check if the vehicle was the cannon
            if (Objects.equals(String.valueOf(player.getVehicle().getScoreboardTags()), String.valueOf(cannonCheck))) {
                // Check if its the first time entering the cannon ( Work around spigot bug )
                if (firstTimeInCannon.containsKey(player.getName())) {
                    // Check if it's the players first time in a cannon
                    if (firstTimeInCannon.get(player.getName())) {
                        firstTimeInCannon.remove(player.getName());
                        firstTimeInCannon.put(player.getName(), false);
                    } else {
                        // If the player is not registered that means that the value is already false so the player can shoot
                        event.setCancelled(true);

                        // Set Cooldown
                        if (cooldowns.containsKey(player.getName())) {
                            // Check if player has cooldown
                            if (cooldowns.get(player.getName()) > System.currentTimeMillis()) {
                                // Still have cooldown
                                long timeleft = (cooldowns.get(player.getName()) - System.currentTimeMillis()) / 1000;
                                player.sendMessage(ChatColor.GOLD + "You still have " + timeleft + " seconds till you use can use this !");
                                return;
                            }
                        }

                        // Player has no cooldown so we add it now
                        cooldowns.put(player.getName(), System.currentTimeMillis() + (3 * 1000));

                        // Launch Arrow / Bullet
                        Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
                        Location location = arrow.getLocation();
                        location.setY(location.getY() + 0.5);
                        Arrow arrowx = player.getWorld().spawnArrow(location, arrow.getVelocity(), 5.0F, 4.0F);
                        arrowx.setShooter(player);
                        arrow.remove();
                    }
                } else {
                    // Set player on hashmap
                    firstTimeInCannon.put(player.getName(), false);
                }
            }
        }
        // Check if the user is trying to place the Cannon on the ground
        else if (player.getInventory().getItemInMainHand() == ItemManager.Cannon || player.getInventory().getItemInOffHand() == ItemManager.Cannon) {
            // If he is trying to palce don't allow
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can't do that !");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // If there is not clicked inventory return nothing
        if (event.getClickedInventory() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        // Check if player clicked in Shop GUI
        if(event.getClickedInventory().getHolder() instanceof shopGUI) {
            // Cancle event so the player can't move items inside of GUI
            event.setCancelled(true);

            // Check if User left clicked
            if(event.isLeftClick()) {
                // If user didn't click on any item return nothing
                if(event.getCurrentItem() == null) {
                    return;
                }

                // Check what the user choose to buy
                if(event.getCurrentItem().getType() == ItemManager.Cannon.getType()) {
                    shopHandler(12, ItemManager.Cannon, player, 1);
                } else if(event.getCurrentItem().getType() == ItemManager.Flintknock.getType()) {
                    shopHandler(6, ItemManager.Flintknock, player, 1);
                } else if(event.getCurrentItem().getType() == ItemManager.PirateSword.getType()) {
                    shopHandler(3, ItemManager.PirateSword, player, 1);
                } else if(event.getCurrentItem().getType() == ItemManager.Bullet.getType()) {
                    shopHandler(2, ItemManager.Bullet, player, 32);
                } else if(event.getCurrentItem().getType() == ItemManager.HealingPotion.getType()) {
                    shopHandler(3, ItemManager.HealingPotion, player, 1);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        // Check if player is in vehicle
        if(!player.isInsideVehicle()) {
            // If isn't well that means that when he enters a cannon its going to be the first time
            firstTimeInCannon.remove(player.getName());
            firstTimeInCannon.put(player.getName(), true);
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        Player player = (Player) event.getEntity();

        // Check if the bow meta is the flintknock's meta
        if(event.getBow().getItemMeta().equals(ItemManager.Flintknock.getItemMeta())){
            // Set Cooldown:
            if(cooldowns.containsKey(player.getName())){
                // Check if player has cooldown
                if(cooldowns.get(player.getName()) > System.currentTimeMillis()){
                    // Still has cooldown
                    long timeleft = (cooldowns.get(player.getName()) - System.currentTimeMillis()) / 1000;
                    player.sendMessage(ChatColor.GOLD + "You still have " + timeleft + " seconds till you use can use this !");
                    event.setCancelled(true);
                    return;
                }
            }

            // Add Cooldown since there is none
            cooldowns.put(player.getName(), System.currentTimeMillis() + (3 * 1000));

            // Push player back when he shoots with Flintknock
            Vector directionVector = player.getLocation().getDirection().normalize();
            player.setVelocity(player.getVelocity().add(directionVector.multiply(-1.3)));
        }
    }

    @EventHandler
    public void hit(ProjectileHitEvent event){
        // Check the Entity is a instance of a arrow and the shooter is the player
        if(event.getEntity() instanceof Arrow &
                event.getEntity().getShooter() instanceof Player){
            // Get the arrow and the player
            Arrow arrow = (Arrow) event.getEntity();
            Player player = (Player) arrow.getShooter();
            // Check if the player is in a vehicle
            assert player != null;
            if(player.isInsideVehicle()){
                // Check if that vehicle is the cannon
                if(Objects.equals(String.valueOf(player.getVehicle().getScoreboardTags()), String.valueOf(cannonCheck))) {
                    Location loc = arrow.getLocation();

                    World world = loc.getWorld();
                    double x = loc.getX();
                    double y = loc.getY();
                    double z = loc.getZ();

                    assert world != null;
                    world.createExplosion(
                            x,
                            y,
                            z,
                            4, //size
                            false, // set fire
                            false); // block break
                }
            }
        }
    }

    public void shopHandler(int price, ItemStack itemBought, Player player, int amountToGive) {
        if(player.getInventory().containsAtLeast(ItemManager.Coin, price)) {
            ItemStack itemGive = new ItemStack(itemBought.getType(), amountToGive);
            itemGive.setItemMeta(itemBought.getItemMeta());
            player.getInventory().addItem(itemGive);
            player.sendMessage(ChatColor.GREEN + "You bought a " + itemBought.getItemMeta().getDisplayName() + "!");
            removeItems(player.getInventory(), ItemManager.Coin.getType(), 12);
        } else {
            player.sendMessage(ChatColor.RED + "You do not have enough §bV-Coins !");
        }
    }

    public static void removeItems(Inventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

}
