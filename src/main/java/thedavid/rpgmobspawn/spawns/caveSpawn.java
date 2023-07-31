package thedavid.rpgmobspawn.spawns;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.joml.Vector2d;

import java.util.*;

import static thedavid.rpgmobspawn.spawns.spawnMobs.spawnMob;

public class caveSpawn implements Listener {
    private final Map<Player, Chunk[]> playerOldChunks = new HashMap<>();
    public static final List<Material> spawnAbleTypes = Arrays.asList(
            Material.ICE,
            Material.BLUE_ICE,
            Material.PACKED_ICE
    );
    public static final List<Material> testLocationSpawnAbleTypes = Arrays.asList(
            Material.MOSS_CARPET,
            Material.SCULK_VEIN,
            Material.GLOW_LICHEN
    );
    public static Map<Vector2d, Map<Integer, Location>> canSpawnLocations = new HashMap<>();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(e.getFrom().getChunk().equals(e.getTo().getChunk())){
            return;
        }
        if(playerOldChunks.containsKey(e.getPlayer())){
            List<Chunk> checkList = Arrays.asList(playerOldChunks.get(e.getPlayer()));
//            e.getPlayer().sendMessage(
//                    Component.text(checkList.toString())
//            );
            Chunk[] chunks = playerOldChunks.get(e.getPlayer());
            for (int i = 0; i < chunks.length - 1; i++) {
                chunks[i] = chunks[i + 1];
            }
            chunks[9] = e.getFrom().getChunk();
            playerOldChunks.put(e.getPlayer(), chunks);
            if(checkList.contains(e.getTo().getChunk())){
//                e.getPlayer().sendMessage(
//                        Component.text("walked chunk" + checkList)
//                );
                return;
            }
        }else {
            Chunk[] chunks = new Chunk[10];
            chunks[9] = e.getFrom().getChunk();
            playerOldChunks.put(e.getPlayer(), chunks);
        }
        int chunkX = e.getTo().getChunk().getX();
        int chunkZ = e.getTo().getChunk().getZ();
        int spawnChunkX;
        int spawnChunkZ;
        spawnChunkX = chunkX;
        spawnChunkZ = chunkZ;
        int r = getRandom(0,3);
        if(r == 0){
            spawnChunkX += 2;
            spawnChunkZ += getRandom(-2,2);
        }else if(r == 1){
            spawnChunkX += getRandom(-2,2);
            spawnChunkZ += 2;
        }else if(r == 2){
            spawnChunkX -= 2;
            spawnChunkZ += getRandom(-2,2);
        }else if(r == 3){
            spawnChunkX += getRandom(-2,2);
            spawnChunkZ -= 2;
        }
        int spawnLocX = getRandom(0,15);
        int spawnLocZ = getRandom(0,15);
        Location spawnLoc = e.getPlayer().getWorld().getChunkAt(spawnChunkX,spawnChunkZ).getBlock(spawnLocX, -64, spawnLocZ).getLocation();
//        e.getPlayer().sendMessage(
//                Component.text(spawnLoc + "caves: " + countCaves(spawnLoc))
//        );
        for(int i = 0; i<10; i++){
            int caves = countCaves(spawnLoc);
            if(caves > 0){
//                Bukkit.getLogger().info("trying to spawn: " + new Vector2d(spawnLoc.getBlockX(), spawnLoc.getBlockZ()));
//                Bukkit.getLogger().info("canSpawnLocations.get(new Vector2d(spawnLoc.getBlockX(), spawnLoc.getBlockZ())): " + canSpawnLocations.get(new Vector2d(spawnLoc.getBlockX(), spawnLoc.getBlockZ())));
//                Bukkit.getLogger().info(String.valueOf(canSpawnLocations));
                for(int j = getRandom(0,2); j>0; j--){
                    if(spawnLoc.getChunk().getEntities().length >= 14 || caves == 0){
                        break;
                    }
                    spawnLoc = canSpawnLocations.get(new Vector2d(spawnLoc.getBlockX(), spawnLoc.getBlockZ())).get(getRandom(1, caves) - 1);
                    spawnMob(spawnLoc, 1);
                    canSpawnLocations.remove(new Vector2d(spawnLoc.getBlockX(), spawnLoc.getBlockZ()));
                    spawnLocX = getRandom(0,15);
                    spawnLocZ = getRandom(0,15);
                    spawnLoc = e.getPlayer().getWorld().getChunkAt(spawnChunkX,spawnChunkZ).getBlock(spawnLocX, 319, spawnLocZ).getLocation();
                    caves = countCaves(spawnLoc);
                }
            }
            spawnChunkX = chunkX;
            spawnChunkZ = chunkZ;
            r = getRandom(0,3);
            if(r == 0){
                spawnChunkX += 2;
                spawnChunkZ += getRandom(-2,2);
            }else if(r == 1){
                spawnChunkX += getRandom(-2,2);
                spawnChunkZ += 2;
            }else if(r == 2){
                spawnChunkX -= 2;
                spawnChunkZ += getRandom(-2,2);
            }else if(r == 3){
                spawnChunkX += getRandom(-2,2);
                spawnChunkZ -= 2;
            }
            spawnLocX = getRandom(0,15);
            spawnLocZ = getRandom(0,15);
            spawnLoc = e.getPlayer().getWorld().getChunkAt(spawnChunkX,spawnChunkZ).getBlock(spawnLocX, 319, spawnLocZ).getLocation();
        }
    }
    public static int countCaves(Location location) {
        int i = -64;
        int caves = 0;
        while (true) {
            location.setY(i);
            if (location.getBlock().getType().isOccluding() || spawnAbleTypes.contains(location.getBlock().getType())) {
                if (location.getBlock().isCollidable() && location.getBlock().isSolid()) {
                    Location testLocation = location.clone();
                    testLocation.setY(i + 1);
                    if ((!testLocation.getBlock().isCollidable() || testLocationSpawnAbleTypes.contains(testLocation.getBlock().getType())) && !testLocation.getBlock().isLiquid() && !(testLocation.getBlock().getType() == Material.AIR)) {
                        testLocation.setY(i + 2);
                        if ((!testLocation.getBlock().isCollidable() || testLocationSpawnAbleTypes.contains(testLocation.getBlock().getType())) && !testLocation.getBlock().isLiquid() && !(testLocation.getBlock().getType() == Material.AIR)) {
                            Location spawnLocation = location.clone();
                            spawnLocation.setY(i + 1); // Adjust Y-level to one block above the solid ground
                            Vector2d vector = new Vector2d(spawnLocation.getBlockX(), spawnLocation.getBlockZ());
                            if (Objects.equals(canSpawnLocations.get(vector), null)) {
                                canSpawnLocations.put(vector, new HashMap<>());
                            }
                            canSpawnLocations.get(vector).put(caves, spawnLocation);
//                            Bukkit.getLogger().info(String.valueOf(spawnLocation));
//                            Bukkit.getLogger().info(String.valueOf(canSpawnLocations));
                            caves++;
                        }
                    }
                }
            }
            if (location.getBlock().getType() == Material.AIR) {
                return caves;
            }
            i++;
        }
    }

    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }

}
