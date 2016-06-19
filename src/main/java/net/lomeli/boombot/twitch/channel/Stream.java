package net.lomeli.boombot.twitch.channel;

public class Stream {
    public boolean is_playlist;
    public ChannelInfo channel;

    public Stream() {
    }

    public class ChannelInfo {
        public boolean mature;
        public String status;
        public String url;
        public String game;
        public String broadcaster_language;

        public ChannelInfo() {
        }
    }
}
