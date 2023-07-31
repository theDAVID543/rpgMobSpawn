package thedavid.rpgmobspawn;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.PlayerLevelUpEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.interaction.util.DurabilityItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.TierManager;
import net.Indyuce.mmoitems.manager.TypeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class mmocoreUtils implements Listener, CommandExecutor {
    public static final List<Material> ores = Arrays.asList(
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.COAL_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_REDSTONE_ORE
    );
    public static final Map<Integer, Double> levelDropRate = new HashMap<Integer, Double>(){{
        put(1, 2.5);
        put(2, 3d);
        put(3, 3.6);
        put(4, 4.32);
        put(5, 5.184);
        put(6, 6.2208);
        put(7, 7.46496);
        put(8, 8.957952);
        put(9, 10.7495424);
        put(10, 12.89945088);
    }};
    @EventHandler
    public void onPlayerJump(PlayerJumpEvent e){
        PlayerData playerData = PlayerData.get(e.getPlayer());
        if(playerData.getStamina() < 3 && e.getPlayer().isSprinting()){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onMoveLevelUp(PlayerLevelUpEvent e){
        if(e.getProfession().getName().equals("moving")){
            e.getPlayer().setWalkSpeed(0.2f);
        }
    }
    @EventHandler
    public void onPlayerHealth(EntityRegainHealthEvent e){
        if(e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)){
            if(PlayerData.get((Player) e.getEntity()).isInCombat()){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBreakBLock(BlockBreakEvent e){
        if(e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.DEEPSLATE_IRON_ORE){
            Material mainHand = e.getPlayer().getInventory().getItemInMainHand().getType();
            if(! (mainHand == Material.STONE_PICKAXE || mainHand == Material.DIAMOND_PICKAXE || mainHand == Material.IRON_PICKAXE || mainHand == Material.NETHERITE_PICKAXE)){
                return;
            }
            e.setDropItems(false);
            dropOre(e.getBlock().getLocation(), "UNSTABLE_MAGIC_IRON_NUGGET", true);

        }else if (e.getBlock().getType() == Material.DIAMOND_ORE || e.getBlock().getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            Material mainHand = e.getPlayer().getInventory().getItemInMainHand().getType();
            if(! (mainHand == Material.DIAMOND_PICKAXE || mainHand == Material.IRON_PICKAXE || mainHand == Material.NETHERITE_PICKAXE)){
                return;
            }
            e.setDropItems(false);
            dropOre(e.getBlock().getLocation(), "UNSTABLE_MAGIC_DIAMOND_PIECE", true);

        }else if (e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.DEEPSLATE_COAL_ORE) {
            Material mainHand = e.getPlayer().getInventory().getItemInMainHand().getType();
            if(! (mainHand == Material.STONE_PICKAXE || mainHand == Material.DIAMOND_PICKAXE || mainHand == Material.IRON_PICKAXE || mainHand == Material.NETHERITE_PICKAXE)){
                return;
            }
            e.setDropItems(false);
            dropOre(e.getBlock().getLocation(), "NORMAL_COAL_PIECE", true);
        }else if (e.getBlock().getType() == Material.REDSTONE_ORE || e.getBlock().getType() == Material.DEEPSLATE_REDSTONE_ORE) {
            Material mainHand = e.getPlayer().getInventory().getItemInMainHand().getType();
            if(! (mainHand == Material.DIAMOND_PICKAXE || mainHand == Material.IRON_PICKAXE || mainHand == Material.NETHERITE_PICKAXE)){
                return;
            }
            e.setDropItems(false);
            double random = getRandom(0,4);
            String oreID = null;
            if(random <= 1){
                oreID = "WEAPON_ATTACK";
            }else if(random <= 2){
                oreID = "WEAPON_ATTACKSPEED";
            }else if(random <= 3){
                oreID = "WEAPON_BLOCKRATE";
            }else if(random <= 4){
                oreID = "WEAPON_DODGERATE";
            }
            TierManager tiers = MMOItems.plugin.getTiers();
            ItemTier UNCOMMON = tiers.get("UNCOMMON");
            MMOItem mmoItem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get("GEM_STONE"), oreID, getLevel(e.getBlock().getLocation()), UNCOMMON);
            if(Objects.equals(mmoItem, null)){
                return;
            }
            ItemStack item = mmoItem.newBuilder().build();
            e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation().toCenterLocation(), item);
        }else if (e.getBlock().getType() == Material.EMERALD_ORE || e.getBlock().getType() == Material.DEEPSLATE_EMERALD_ORE) {
            Material mainHand = e.getPlayer().getInventory().getItemInMainHand().getType();
            if(! (mainHand == Material.DIAMOND_PICKAXE || mainHand == Material.IRON_PICKAXE || mainHand == Material.NETHERITE_PICKAXE)){
                return;
            }
            e.setDropItems(false);
            double random = getRandom(0,2);
            String oreID = null;
            if(random <= 1){
                oreID = "ARMOR_DEFENSE";
            }else if(random <= 2){
                oreID = "ARMOR_SPEED";
            }
            TierManager tiers = MMOItems.plugin.getTiers();
            ItemTier UNCOMMON = tiers.get("UNCOMMON");
            MMOItem mmoItem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get("GEM_STONE"), oreID, getLevel(e.getBlock().getLocation()), UNCOMMON);
            if(Objects.equals(mmoItem, null)){
                return;
            }
            ItemStack item = mmoItem.newBuilder().build();
            e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation().toCenterLocation(), item);
        }
    }
    public void dropOre(@NotNull Location location, @NotNull String oreID, @NotNull Boolean addDrop){
        MMOItem mmoItem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get("MATERIAL"), oreID);
        if(Objects.equals(mmoItem, null)){
            return;
        }
        ItemStack item = mmoItem.newBuilder().build();
        if(addDrop){
            item.add(getDrops(location) - 1);
        }
        location.getWorld().dropItem(location.toCenterLocation(), item);
    }
    public int getLevel(Location location){
        Location zeroLoc = new Location(location.getWorld(), 0, 0, 0);
        double distance = location.distance(zeroLoc);
        int level = (int) (distance / 500) + 1;
        return level;
    }
    public int getDrops(Location location){
        int level = getLevel(location);
        int drops = 1;
//        levelDropRate.putIfAbsent(level, 0.0);
//        for(int i = 0; i<level; i++){
//            if(ifDrop(75d)){
//                drops++;
//            }
//        }
        Double dropRate = levelDropRate.get(level);
        int i;
        for(i = 1; dropRate > 0.5; i *= 2){
            dropRate /= 2;
        }
//        Bukkit.getLogger().info("i: " + i + " dropRate: " + dropRate + " levelDropRate: " + levelDropRate.get(level));
        for(int j = 0; j < i; j++){
            if(ifDrop(dropRate)){
                drops++;
            }
        }
        return drops;
    }
    public boolean ifDrop(Double rate){
        double random = getRandom(0,1);
//        Bukkit.getLogger().info(String.valueOf(random));
        return random <= rate;
    }
    public double getRandom(double lower, double upper) {
        Random random = new Random();
        return random.nextDouble((upper - lower)) + lower;
    }
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e){
        if(ores.contains(e.getBlock().getType()) && !e.getPlayer().isOp()){
            e.setCancelled(true);
            e.getPlayer().sendMessage(
                    Component.text("此世界無法放置: ").color(NamedTextColor.RED)
                            .append(Component.translatable(e.getBlock().getType().translationKey()).color(NamedTextColor.DARK_AQUA))
            );
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(args[0].equals("perfusion")){
            Player player = (Player)sender;
            String itemName = args[1];
            if(itemName.equals("MAGIC_COAL")){
                if(player.getLevel() >= 20){
                    player.setLevel(player.getLevel() - 20);
                    MMOItem magicCoal = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get("MATERIAL"), "MAGIC_COAL");
                    if(Objects.equals(magicCoal, null)){
                        return true;
                    }
                    ItemStack item = magicCoal.newBuilder().build();
                    player.getInventory().addItem(item);
                }else{
                    MMOItem normalCoal = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get("CONSUMABLE"), "NORMAL_COAL");
                    if(Objects.equals(normalCoal, null)){
                        return true;
                    }
                    ItemStack item = normalCoal.newBuilder().build();
                    player.getInventory().addItem(item);
                }
            }
        } else if (args[0].equals("test")) {
            Player player = (Player)sender;
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            NBTItem nbtItem = NBTItem.get(mainHandItem);
            DurabilityItem durItem = new DurabilityItem(player, mainHandItem);
//            if (durItem.isValid()) {
                int currentDura = durItem.getDurability();
                int maxDura = durItem.getMaxDurability();
                player.sendMessage(
                        Component.text()
                                .append(Component.text("currentDura: " + currentDura))
                                .appendNewline()
                );
                durItem.decreaseDurability(1);
                ItemMeta result = durItem.toItem().getItemMeta();
                ItemStack finalCopy = new ItemStack(mainHandItem);
                finalCopy.setItemMeta(result);
                player.getInventory().setItemInMainHand(finalCopy);
//            }
            player.sendMessage(
                    Component.text()
                            .append(Component.text("nbtItem.getStat(\"DURABILITY\"): " + nbtItem.getStat("DURABILITY")))
                            .appendNewline()
                            .append(Component.text("durItem: " + durItem))
            );
            Location location = player.getLocation();
            if(location.getBlock().getType() == Material.WATER_CAULDRON){

            }
        }
        return true;
    }
    @EventHandler
    public void onTameEntity(EntityTameEvent e){
        if(e.getEntity().getType() == EntityType.HORSE){
            e.setCancelled(true);
            Player player = (Player)e.getOwner();
            player.sendMessage(
                    Component.text("此RPG分流無法馴服馬").color(NamedTextColor.RED)
            );
        }
    }
    @EventHandler
    public void onElytraFly(EntityToggleGlideEvent e){
        if(e.isGliding() && e.getEntity() instanceof Player){
            Player player = (Player)e.getEntity();
            ItemStack elytra = player.getInventory().getChestplate();
            ItemMeta elytraMeta = elytra.getItemMeta();
            Damageable damageableElytra = (Damageable) elytraMeta;
            damageableElytra.setDamage(damageableElytra.getDamage() + 10);
            player.sendMessage(
                    Component.text("在這個魔法大陸空氣中的魔力會擾亂鞘翅").color(NamedTextColor.RED)
                            .appendNewline()
                            .append(Component.text("無法使用 鞘翅"))
            );
            elytra.setItemMeta(damageableElytra);
            e.setCancelled(true);
        }
    }
}
