package cn.mcdawncity.superfood.utils;

import lombok.NonNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.config.PConfig;

import java.io.File;

public class Configuration extends PConfig {

    public static boolean VAULT;

    public Configuration(@NonNull PPlugin plugin) {
        super(plugin, "config", "主配置文件");
        if (plugin == null)
            throw new NullPointerException("plugin is marked non-null but is null");
    }

    @Override
    public void saveDefault() {
        plugin.saveDefaultConfig();
    }

    @Override
    public void load(File file) {
        VAULT = getConfig().getBoolean("Hooks.Vault");
    }
}
