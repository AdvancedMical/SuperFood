package cn.mcdawncity.superfood.utils;

import cn.mcdawncity.superfood.SuperFood;
import lombok.NonNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.config.PConfig;

public class Foods extends PConfig {
    private static Foods foods;

    public Foods(@NonNull PPlugin plugin) {
        super(plugin, "foods", "食物列表");
        if (plugin == null)
            throw new NullPointerException("plugin is marked non-null but is null");
    }

    public static Foods get(){
        if (foods == null)
            foods = new Foods(SuperFood.getInstance());
        return foods;
    }

    @Override
    public void saveDefault() {
        super.saveDefault();
        this.plugin.saveResource("Foods.yml", false);
    }
}
