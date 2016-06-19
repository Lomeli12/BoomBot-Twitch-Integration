package net.lomeli.boombot.twitch.channel;

import com.google.common.collect.Lists;

import java.util.List;

public class ChannelResponse {
    public List<Stream> streams;

    public ChannelResponse(){
        this.streams = Lists.newArrayList();
    }

    public Stream getLiveStream() {
        if (streams != null && !streams.isEmpty()) {
            for (Stream stream : streams) {
                if (stream != null && !stream.is_playlist)
                    return stream;
            }
        }
        return null;
    }
}
