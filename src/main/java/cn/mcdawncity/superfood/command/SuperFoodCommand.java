package cn.mcdawncity.superfood.command;

import lombok.NonNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.CommandHandler;
import org.serverct.parrot.parrotx.command.subcommands.HelpCommand;
import org.serverct.parrot.parrotx.command.subcommands.ReloadCommand;

public class SuperFoodCommand extends CommandHandler {
    public SuperFoodCommand(@NonNull PPlugin plugin, String mainCmd) {
        super(plugin, mainCmd);
        if (plugin == null)
            throw new NullPointerException("plugin is marked non-null but is null");
        registerSubCommand("reload", new ReloadCommand(plugin, "SuperFood.admin"));
        registerSubCommand("help", new HelpCommand(plugin, "SuperFood.use"));
    }
}
