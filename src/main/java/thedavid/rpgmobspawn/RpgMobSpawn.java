package thedavid.rpgmobspawn;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmoitems.api.crafting.MMOItemUIFilter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import thedavid.rpgmobspawn.spawns.caveSpawn;
import thedavid.rpgmobspawn.spawns.groundSpawn;

import java.util.*;

public final class RpgMobSpawn extends JavaPlugin {
    public static JavaPlugin instance;
    public final Map<Player, Integer> playerSprintTime = new HashMap<>();
    public static Set<Player> sprintPlayer = new HashSet<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(new groundSpawn(), this);
        getServer().getPluginManager().registerEvents(new caveSpawn(), this);
        getServer().getPluginManager().registerEvents(new mmocoreUtils(), this);
        getServer().getPluginManager().registerEvents(new mmoItemRecipe(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("rpgmobspawn")).setExecutor(new commandHandler());
        Objects.requireNonNull(Bukkit.getPluginCommand("mmoutils")).setExecutor(new mmocoreUtils());
        Objects.requireNonNull(Bukkit.getPluginCommand("mmorecipe")).setExecutor(new mmoItemRecipe());
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    PlayerData playerData = PlayerData.get(player);
                    if(player.isSprinting() && !player.isFlying()){
                        playerSprintTime.putIfAbsent(player, 0);
                        playerSprintTime.put(player, playerSprintTime.get(player) + 1);
                        if(playerSprintTime.get(player) > 1){
                            playerData.setStamina(playerData.getStamina() - 0.1);
                            playerSprintTime.put(player, 0);
                        }
                        if(playerData.getStamina() < 3){
                            player.setWalkSpeed(0.025F);
                        }else{
                            player.setWalkSpeed(0.2F);
                        }
                    }else{
                        if(playerData.getStamina() < 3){
                            player.setWalkSpeed(0.2F);
                        }
                        if(!playerData.isInCombat() && playerData.getStamina() < 20){
                            player.setWalkSpeed(0.2F);
                            playerData.setStamina(playerData.getStamina() + 0.1);
                        }
                    }
                }
            }
        }.runTaskTimer(instance, 0, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
