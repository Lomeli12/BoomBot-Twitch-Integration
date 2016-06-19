package net.lomeli.boombot.twitch.config;

public class Client {
    private String channelName;
    private String userID;

    public Client() {
    }

    public Client(String channelName, String userID) {
        this.channelName = channelName;
        this.userID = userID;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getUserID() {
        return userID;
    }

}
