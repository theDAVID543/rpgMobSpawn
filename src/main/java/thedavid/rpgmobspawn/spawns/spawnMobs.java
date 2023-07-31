package thedavid.rpgmobspawn.spawns;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Location;

import java.util.Random;

public class spawnMobs {
    public static void spawnMob(Location spawnlocation, int group){
        if(group == 1){
            spawnlocation.toCenterLocation();
            int random = getRandom(0,10);
            MythicMob mob = null;
            if(random <= 2){
                mob = MythicBukkit.inst().getMobManager().getMythicMob("NormalZombie").orElse(null);
            }else if(random == 3){
                mob = MythicBukkit.inst().getMobManager().getMythicMob("ChargeZombie").orElse(null);
            }else if(random == 4){
                mob = MythicBukkit.inst().getMobManager().getMythicMob("HeavyZombie").orElse(null);
            }else if(random <= 7){
                mob = MythicBukkit.inst().getMobManager().getMythicMob("NormalSpider").orElse(null);
            }else if(random == 8){
                mob = MythicBukkit.inst().getMobManager().getMythicMob("ChargeSpider").orElse(null);
            }else if(random <= 10){
                mob = MythicBukkit.inst().getMobManager().getMythicMob("NormalSkeleton").orElse(null);
            }
            if(mob != null){
                Location zeroLoc = new Location(spawnlocation.getWorld(), 0, 0, 0);
                double distance = spawnlocation.distance(zeroLoc);
                int level = (int) (distance / 500) + 1;
                mob.spawn(BukkitAdapter.adapt(spawnlocation), level);
            }
        }
    }
    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }
}
