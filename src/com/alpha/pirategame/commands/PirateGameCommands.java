package com.alpha.pirategame.commands;

import com.alpha.pirategame.items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PirateGameCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] strings) {
        Player player = (Player) commandSender;

        // Give Player Shop Spawner
        if("getshopspawner".equalsIgnoreCase(cmd.getName())) {
            player.getInventory().addItem(ItemManager.ShopSpawner);
        }

        return true;
    }
}
