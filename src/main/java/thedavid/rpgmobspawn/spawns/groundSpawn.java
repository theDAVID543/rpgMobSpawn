package thedavid.rpgmobspawn.spawns;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

import static thedavid.rpgmobspawn.spawns.spawnMobs.spawnMob;

public class groundSpawn implements Listener {
    public static final List<Material> spawnAbleTypes = Arrays.asList(
            Material.ICE,
            Material.BLUE_ICE,
            Material.PACKED_ICE
    );
    public static final List<Material> notSpawnAbleTypes = Arrays.asList(
            Material.ACACIA_LEAVES,
            Material.BIRCH_LEAVES,
            Material.CHERRY_LEAVES,
            Material.OAK_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.AZALEA_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.MANGROVE_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.BIRCH_LOG,
            Material.OAK_LOG,
            Material.ACACIA_LOG,
            Material.CHERRY_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.MANGROVE_LOG,
            Material.SPRUCE_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_CHERRY_LOG,
            Material.STRIPPED_MANGROVE_LOG,
            Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_SPRUCE_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.ACACIA_WOOD,
            Material.BIRCH_WOOD,
            Material.CHERRY_WOOD,
            Material.DARK_OAK_WOOD,
            Material.JUNGLE_WOOD,
            Material.MANGROVE_WOOD,
            Material.OAK_WOOD,
            Material.SPRUCE_WOOD
    );
    public static final List<Material> testLocationSpawnAbleTypes = Arrays.asList(
            Material.AIR,
            Material.GRASS,
            Material.TALL_GRASS,
            Material.SNOW,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.FERN,
            Material.DEAD_BUSH,
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.WHITE_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.PINK_PETALS,
            Material.LARGE_FERN,
            Material.SUNFLOWER,
            Material.LILAC,
            Material.ROSE_BUSH,
            Material.PEONY

    );

    private final Map<Player, Chunk[]> playerOldChunks = new HashMap<>();
    private final Map<Player, List<Chunk>> allPlayerChunks = new HashMap<>();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(e.getFrom().getChunk().equals(e.getTo().getChunk())){
            return;
        }
        if(Objects.equals(allPlayerChunks.get(e.getPlayer()), null)){
            allPlayerChunks.put(e.getPlayer(), new ArrayList<>());
        }
        allPlayerChunks.get(e.getPlayer()).add(e.getFrom().getChunk());
        if(!allPlayerChunks.get(e.getPlayer()).contains(e.getTo().getChunk())){
            e.getPlayer().setWalkSpeed(0.2F);
            MMOCore.plugin.professionManager.get("moving").giveExperience(PlayerData.get(e.getPlayer().getUniqueId()),10,null, EXPSource.VANILLA);
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
        if(Objects.equals(playerOldChunks.get(e.getPlayer())[0],null)){
            return;
        }
        spawnChunkX = chunkX + (chunkX - playerOldChunks.get(e.getPlayer())[5].getX());
        spawnChunkZ = chunkZ + (chunkZ - playerOldChunks.get(e.getPlayer())[5].getZ());
        spawnChunkX += getRandom(-3,3);
        spawnChunkZ += getRandom(-3,3);
        int spawnLocX = getRandom(0,15);
        int spawnLocZ = getRandom(0,15);
        Location spawnLoc = e.getPlayer().getWorld().getChunkAt(spawnChunkX,spawnChunkZ).getBlock(spawnLocX, 319, spawnLocZ).getLocation();
        spawnLoc = spawnLoc.toHighestLocation();
//        for(int i = (int) spawnLoc.getY(); !spawnLoc.getBlock().getType().isTransparent() && !spawnLoc.add(0,1,0).getBlock().getType().equals(Material.AIR); i--){
//            e.getPlayer().sendMessage(Component.text(spawnLoc.toString()));
//        }
        for(int i = 0; i<10; i++){
            if(spawn(spawnLoc)){
//                e.getPlayer().sendMessage(
//                        Component.text("spawned")
//                );
                for(int j = getRandom(0,2); j>0; j--){
                    if(spawnLoc.getChunk().getEntities().length >= 14){
                        break;
                    }
                    spawn(spawnLoc);
                    spawnLocX = getRandom(0,15);
                    spawnLocZ = getRandom(0,15);
                    spawnLoc = e.getPlayer().getWorld().getChunkAt(spawnChunkX,spawnChunkZ).getBlock(spawnLocX, 319, spawnLocZ).getLocation();
                    spawnLoc = spawnLoc.toHighestLocation();
                }
                break;
            }else{
                spawnChunkX = chunkX + (chunkX - playerOldChunks.get(e.getPlayer())[5].getX());
                spawnChunkZ = chunkZ + (chunkZ - playerOldChunks.get(e.getPlayer())[5].getZ());
                spawnChunkX += getRandom(-3,3);
                spawnChunkZ += getRandom(-3,3);
                spawnLocX = getRandom(0,15);
                spawnLocZ = getRandom(0,15);
                spawnLoc = e.getPlayer().getWorld().getChunkAt(spawnChunkX,spawnChunkZ).getBlock(spawnLocX, 319, spawnLocZ).getLocation();
                spawnLoc = spawnLoc.toHighestLocation();
            }
        }
    }
    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }
    public static boolean spawn(Location location){
        int i = (int) location.getY();
        while (true){
            location.setY(i);
            if(location.getBlock().getType().isOccluding() || spawnAbleTypes.contains(location.getBlock().getType())){
                if(location.getBlock().isCollidable() && location.getBlock().isSolid() && !notSpawnAbleTypes.contains(location.getBlock().getType())){
                    Location testLocation = location;
                    testLocation.setY(i+1);
                    if((!testLocation.getBlock().isCollidable() || testLocationSpawnAbleTypes.contains(testLocation.getBlock().getType()) )&& !testLocation.getBlock().isLiquid()  && !(testLocation.getBlock().getType() == Material.CAVE_AIR)){
                        testLocation.setY(i+2);
                        if((!testLocation.getBlock().isCollidable() || testLocationSpawnAbleTypes.contains(testLocation.getBlock().getType()) )&& !testLocation.getBlock().isLiquid() && !(testLocation.getBlock().getType() == Material.CAVE_AIR)){
                            spawnMob(location, 1);
                            return true;
                        }
                    }
                }
            }
            if(i<=64){
                return false;
            }
            i--;
        }
    }
}
