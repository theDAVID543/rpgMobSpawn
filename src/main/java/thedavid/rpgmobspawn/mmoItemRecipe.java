package thedavid.rpgmobspawn;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class mmoItemRecipe implements Listener, CommandExecutor {
    private static final Map<Player, Boolean> playerViewingRecipe = new HashMap<>();
    @EventHandler
    public void onClickInv(InventoryClickEvent e){
        ItemStack itemStack = e.getCurrentItem();
        NBTItem nbtItem = NBTItem.get(itemStack);
        Player player = (Player) e.getWhoClicked();
        if(!Objects.equals(playerViewingRecipe.get(player), null) && nbtItem.hasType()){
            if(e.isLeftClick()){
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                player.performCommand("cp " + nbtItem.getString("MMOITEMS_ITEM_ID") + "-r");
            }else if(e.isRightClick()){
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                player.performCommand("cp " + nbtItem.getString("MMOITEMS_ITEM_ID") + "-u");
            }
        }
    }
    BossBar bossbar = BossBar.bossBar(Component.text("查詢合成表模式"), 1f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] arg) {
        if(Objects.equals(playerViewingRecipe.get((Player)sender), null)){
            playerViewingRecipe.put((Player)sender, true);
            sender.sendMessage(
                    Component.text()
                            .append(Component.text("已開啟 查詢合成表模式").color(NamedTextColor.YELLOW))
                            .appendNewline()
                            .append(Component.text("在背包 左鍵點擊物品: 查看合成配方 右鍵點擊物品: 查看用途").color(NamedTextColor.YELLOW))
                            .appendNewline()
                            .append(Component.text("[點我]").clickEvent(ClickEvent.runCommand("/mmorecipe")).color(NamedTextColor.DARK_GRAY).hoverEvent(Component.text("點擊")))
                            .append(Component.text(" 關閉 查詢合成表模式").color(NamedTextColor.RED))
            );
            sender.showBossBar(bossbar);

        }else{
            playerViewingRecipe.put((Player)sender, null);
            sender.sendMessage(
                    Component.text()
                            .append(Component.text("已關閉 查詢合成表模式").color(NamedTextColor.RED))
            );
            bossbar.removeViewer(sender);
        }
        return true;
    }
}
