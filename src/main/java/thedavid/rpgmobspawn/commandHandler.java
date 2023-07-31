package thedavid.rpgmobspawn;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import thedavid.rpgmobspawn.spawns.caveSpawn;
import thedavid.rpgmobspawn.spawns.groundSpawn;

public class commandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length>0 && args[0].equals("countcaves")){
            sender.sendMessage(
                    Component.text(caveSpawn.countCaves(Bukkit.getPlayer(sender.getName()).getLocation()))
            );
        }else{
            sender.sendMessage(
                    Component.text(groundSpawn.spawn(Bukkit.getPlayer(sender.getName()).getLocation()))
            );
        }
        return true;
    }
}
