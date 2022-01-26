/*
*
* Main Class ( Pirate Game )
*
* Version 1.0
*
* 1/26/2022
*
* */

package com.alpha.pirategame;

import com.alpha.pirategame.commands.PirateGameCommands;
import com.alpha.pirategame.events.PirateGameEvents;
import com.alpha.pirategame.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PirateGame extends JavaPlugin {
    @Override
    public void onEnable() {
        // Events Class
        PirateGameEvents events = new PirateGameEvents();

        // Commands Class
        PirateGameCommands commands = new PirateGameCommands();

        /* Start Events */
        getServer().getPluginManager().registerEvents(events, this);

        /* Initialize Items */
        ItemManager.init();

        /* Register Commands */
        getCommand("getshopspawner").setExecutor(commands);

        /* Success Message */
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PirateGame] PirateGame Started");
    }

    @Override
    public void onDisable() {
        /* Shutdown Message */
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PirateGame] PirateGame Shutdown");
    }
}
