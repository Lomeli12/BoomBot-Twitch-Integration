package net.lomeli.boombot.twitch.commands;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.twitch.TwitchIntegration;

public class RemoveChannelCommand extends Command {
    public RemoveChannelCommand() {
        super("removechannel", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            String channel = cmd.getArgs().get(0);
            if (TwitchIntegration.config.removeClient(cmd.getGuild().getId(), channel))
                cmd.sendMessage("Removed %s from track list.", channel);
            else
                cmd.sendMessage("Could not find %s in track list", channel);
        } else
            cmd.sendMessage("Missing Channel name");
    }
}
