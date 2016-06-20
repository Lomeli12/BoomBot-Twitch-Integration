package net.lomeli.boombot.twitch;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;

import net.lomeli.boombot.api.BoomAddon;
import net.lomeli.boombot.api.BoomBotAPI;
import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.twitch.commands.AddChannelCommand;
import net.lomeli.boombot.twitch.commands.RemoveChannelCommand;
import net.lomeli.boombot.twitch.config.TwitchConfig;

@BoomAddon(addonID = TwitchIntegration.ADDON_ID, name = TwitchIntegration.ADDON_NAME, version = TwitchIntegration.VERSION,
        acceptedBoomBotVersion = "2.0.0")
public class TwitchIntegration {
    public static final String ADDON_ID = "boombot_twitch_integration";
    public static final String ADDON_NAME = "BoomBot Twitch Integration";
    public static final String VERSION = "1.0.0";
    public static final File CONFIG_FILE = BoomBotAPI.getAddonConfig(ADDON_ID + ".cfg");

    @BoomAddon.Instance
    public static TwitchIntegration instance;

    public static Thread listenerThread;
    public static TwitchConfig config;
    public static ChannelListener channelListener;

    @BoomAddon.Init
    public void initAddon() {
        parseConfig();
        CommandRegistry.INSTANCE.addNewCommand(new AddChannelCommand());
        CommandRegistry.INSTANCE.addNewCommand(new RemoveChannelCommand());
    }

    @BoomAddon.PostInit
    public void postInitAddon() {
        listenerThread = new Thread(channelListener = new ChannelListener());
        listenerThread.start();
    }

    private static void parseConfig() {
        if (CONFIG_FILE == null)
            return;
        if (!CONFIG_FILE.exists()) {
            config = new TwitchConfig();
            writeConfig();
        }
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String data = FileUtils.readFileToString(CONFIG_FILE, "UTF-8");
            if (!Strings.isNullOrEmpty(data))
                config = gson.fromJson(data, TwitchConfig.class);
        } catch (Exception e) {
            Logger.error("Failed to read config file %s!", e, CONFIG_FILE.toString());
        }
    }

    public static void writeConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String stuff = gson.toJson(config, TwitchConfig.class);
            FileUtils.write(CONFIG_FILE, stuff, "UTF-8");
        } catch (Exception e) {
            Logger.error("Could not write to %s!", e, CONFIG_FILE.toString());
        }
    }
}
