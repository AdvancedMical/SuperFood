package cn.mcdawncity.superfood.listener;

import cn.mcdawncity.superfood.SuperFood;
import cn.mcdawncity.superfood.utils.Foods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.utils.I18n;

public class ConsumeListener implements Listener {

    private SuperFood plugin;

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        plugin = (SuperFood) SuperFood.getInstance();
        Player player = e.getPlayer();
        for (String key : Foods.get().getConfig().getKeys(false)){
            try {
                if (e.getItem().getItemMeta().getDisplayName().equals(Foods.get().getConfig().getString(key + ".name").replace("&", "§")) && e.getItem().getItemMeta().getLore().equals(Foods
                .get().getConfig().getString(key + ".lores").replace("&", "§"))){
                    for (String cmd : Foods.get().getConfig().getStringList(key + ".commands")){
                        double money = plugin.getVaultUtil().getBalances(player);
                        double take = Foods.get().getConfig().getDouble(key + ".money");
                        if (money < take){
                            I18n.send(player, SuperFood.getInstance().lang.getRaw(SuperFood.getInstance().localeKey, "Lang", "no-money-message"));
                        } else {
                            if (Foods.get().getConfig().getDouble(key + ".money") != 0) {
                                plugin.getVaultUtil().take(player, take);
                            }
                            if (player.hasPermission(Foods.get().getConfig().getString(key + ".permission")) || Foods.get().getConfig().getString(key + ".permission").equalsIgnoreCase("none")) {
                                if (cmd != null) {
                                    boolean isOp = player.isOp();
                                    if (Foods.get().getConfig().getBoolean(key + ".op")) {
                                        try {
                                            player.setOp(true);
                                            SuperFood.getInstance().getServer().dispatchCommand((CommandSender) player, cmd.replace("{player}", e.getPlayer().getName().replace("&", "§")));
                                            player.setOp(isOp);
                                        } catch (Exception exception) {

                                        } finally {
                                            player.setOp(isOp);
                                        }
                                        continue;
                                    }
                                    SuperFood.getInstance().getServer().dispatchCommand((CommandSender) player, cmd.replace("{player}", e.getPlayer().getName().replace("&", "§")));
                                }
                                continue;
                            }
                        }
                        I18n.send(player, SuperFood.getInstance().lang.getRaw(SuperFood.getInstance().localeKey, "Lang", "no-permission-message"));
                    }
                }
            } catch (NullPointerException nullPointerException) {}
        }
    }
}
