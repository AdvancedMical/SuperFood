package cn.mcdawncity.superfood;

import cn.mcdawncity.superfood.command.SuperFoodCommand;
import cn.mcdawncity.superfood.listener.ConsumeListener;
import cn.mcdawncity.superfood.utils.Configuration;
import cn.mcdawncity.superfood.utils.Foods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.hooks.VaultUtil;
import org.serverct.parrot.parrotx.utils.I18n;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class SuperFood extends PPlugin implements Listener {

    private VaultUtil vaultUtil;

    public VaultUtil getVaultUtil() {
        return this.vaultUtil;
    }

    String latestVersion;

    boolean isLatest = true;

    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("§cSuperFood插件v" + getDescription().getVersion() + "已经停止!");
    }

    @Override
    protected void preload() {
        this.pConfig = new Configuration(this);
        this.pConfig.init();
    }

    @Override
    protected void load() {
        Foods.get().init();
        this.vaultUtil = new VaultUtil(this, true);
        registerCommand(new SuperFoodCommand(this, "superfood"));
        Bukkit.getConsoleSender().sendMessage("§aSuperFood插件v" + getDescription().getVersion() + "已经启动!");
    }

    @Override
    protected void registerListener() {
        getServer().getPluginManager().registerEvents(new ConsumeListener(), this);
    }

    public void checkUpdate(){
        new BukkitRunnable(){

            @Override
            public void run() {
                lang.log("插件检查更新中...", I18n.Type.INFO, false);
                lang.log("当前版本: §c" + getDescription().getVersion(), I18n.Type.INFO, false);
                lang.log("最新版本: §c" + getLastVersion(), I18n.Type.INFO, false);
                latestVersion = getLastVersion();
                if (latestVersion == null)
                    return;
                if (latestVersion.equalsIgnoreCase(getDescription().getVersion())){
                    lang.log("插件已经是最新版了.", I18n.Type.INFO, false);
                } else {
                    lang.log("插件不是最新版,请立即更新!", I18n.Type.WARN, false);
                    isLatest = false;
                    Bukkit.getOnlinePlayers().forEach(this::sendUpdate);
                    Bukkit.getPluginManager().registerEvents(new Listener() {

                        @EventHandler
                        public void join(PlayerJoinEvent event){
                            sendUpdate(event.getPlayer());
                        }
                    }, SuperFood.this);
                }
            }

            private void sendUpdate(Player player) {
                if (player.hasPermission("SuperFood.admin"))
                    I18n.send(player, lang.build(localeKey, I18n.Type.WARN, "插件不是最新版,请立即更新!"));
            }
        }.runTaskAsynchronously(this);
    }

    public static String getLastVersion(){
        HttpURLConnection connection = null;
        try {
            int timeout = 5000;
            URL url = new URL("https://mcdawncity.coding.net/p/superfood/d/superfood/git/raw/master/checkVersion.txt");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            InputStream inputStream = connection.getInputStream();
            final StringBuilder stringBuilder = new StringBuilder(255);
            int byteRead;
            while ((byteRead = inputStream.read()) != -1) {
                stringBuilder.append((char) byteRead);
            }
            return stringBuilder.toString().trim();
        } catch (Exception exception) {
            SuperFood.getInstance().lang.log("无法获取插件更新信息.", I18n.Type.ERROR, false);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
}
