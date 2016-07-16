package net.lomeli.boombot.twitch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lomeli.boombot.api.BoomBotAPI;
import net.lomeli.boombot.twitch.channel.ChannelResponse;
import net.lomeli.boombot.twitch.channel.Stream;
import net.lomeli.boombot.twitch.config.Client;

public class ChannelListener implements Runnable {
    private long time;
    private boolean isChecking;
    private HashMap<String, List<String>> live;

    public ChannelListener() {
        time = System.currentTimeMillis();
        this.live = Maps.newHashMap();
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= time + 15000) {
                if (!isChecking) {
                    for (Map.Entry<String, List<Client>> entry : TwitchIntegration.config.getGuildClientList().entrySet()) {
                        isChecking = true;
                        String guildId = entry.getKey();
                        List<Client> registeredClients = entry.getValue();
                        if (registeredClients != null && !registeredClients.isEmpty()) {
                            registeredClients.stream().forEach(client -> {
                                String channel = client.getChannelName();
                                ChannelResponse response = isChannelStreaming(channel);
                                if (response != null && response.streams.size() > 0) {
                                    Stream stream = response.getLiveStream();
                                    if (stream != null && !isCachedLive(guildId, channel)) {
                                        cacheLive(guildId, channel);
                                        Stream.ChannelInfo info = stream.channel;
                                        BoomBotAPI.sendMessageToGuild(guildId,
                                                "@everyone, <@%s> is NOW LIVE at https://www.twitch.tv/%s\nPlaying %s | %s | (%s)",
                                                client.getUserID(), channel, info.game, info.status + (info.mature ? " [MATURE]" : ""),
                                                info.broadcaster_language);
                                    }
                                } else if (isCachedLive(guildId, channel))
                                    removeLive(guildId, channel);
                            });
                        }
                    }
                    isChecking = false;
                }
                time = currentTime;
            }
        }
    }

    private boolean isCachedLive(String guild, String channel) {
        List<String> liveList = live.get(guild);
        return liveList == null ? false : liveList.contains(channel);
    }

    private void cacheLive(String guild, String channel) {
        List<String> liveList = live.get(guild);
        if (liveList == null) liveList = Lists.newArrayList();
        liveList.add(channel);
        live.put(guild, liveList);
    }

    public void removeLive(String guild, String channel) {
        List<String> liveList = live.get(guild);
        if (liveList == null || liveList.isEmpty()) return;
        liveList.remove(channel);
        live.put(guild, liveList);
    }

    private ChannelResponse isChannelStreaming(String channel) {
        try {
            URL responseURL = new URL("https://api.twitch.tv/kraken/streams?channel=" + channel);
            InputStreamReader reader = new InputStreamReader(responseURL.openStream());
            ChannelResponse response = new Gson().fromJson(reader, ChannelResponse.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
