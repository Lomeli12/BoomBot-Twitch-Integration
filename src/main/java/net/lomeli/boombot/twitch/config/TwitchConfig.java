package net.lomeli.boombot.twitch.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;

import net.lomeli.boombot.twitch.TwitchIntegration;

public class TwitchConfig {
    private HashMap<String, List<Client>> guildClientList;

    public TwitchConfig() {
        this.guildClientList = Maps.newHashMap();
    }

    public HashMap<String, List<Client>> getGuildClientList() {
        return guildClientList;
    }

    public boolean isChannelListed(String guildID, String channelName) {
        List<Client> clients = guildClientList.get(guildID);
        if (clients != null && !clients.isEmpty()) {
            for (Client client : clients) {
                if (client.getChannelName().equalsIgnoreCase(channelName))
                    return true;
            }
        }
        return false;
    }

    public boolean addClient(String guildID, Client client) {
        if (client == null || isChannelListed(guildID, client.getChannelName())) return false;
        List<Client> clients = guildClientList.get(guildID);
        if (clients == null) clients = Lists.newArrayList();
        clients.add(client);
        guildClientList.put(guildID, clients);
        TwitchIntegration.writeConfig();
        return true;
    }

    public boolean removeClient(String guildID, String channelName) {
        if (isChannelListed(guildID, channelName)) {
            List<Client> clientList = guildClientList.get(guildID);
            if (clientList == null || clientList.isEmpty())
                return false;
            int rmindex = -1;
            for (int i = 0; i < clientList.size(); i++) {
                if (clientList.get(i).getChannelName().equalsIgnoreCase(channelName)) {
                    rmindex = i;
                    break;
                }
            }
            if (rmindex > -1 && rmindex < clientList.size()) {
                clientList.remove(rmindex);
                guildClientList.put(guildID, clientList);
                TwitchIntegration.channelListener.removeLive(guildID, channelName);
                TwitchIntegration.writeConfig();
                return true;
            }
        }
        return false;
    }
}
