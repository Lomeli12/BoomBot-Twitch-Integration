package net.lomeli.boombot.twitch.commands;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.twitch.TwitchIntegration;
import net.lomeli.boombot.twitch.config.Client;

public class AddChannelCommand extends Command {
    public AddChannelCommand() {
        super("addchannel", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            String channel = cmd.getArgs().get(0);
            if (TwitchIntegration.config.addClient(cmd.getGuild().getId(), new Client(channel, cmd.getUser().getId())))
                cmd.sendMessage("Added https://twitch.tv/%s to track list.", channel);
            else
                cmd.sendMessage("Could not add %s to track list", channel);
        } else
            cmd.sendMessage("Missing Channel name");
    }
}
