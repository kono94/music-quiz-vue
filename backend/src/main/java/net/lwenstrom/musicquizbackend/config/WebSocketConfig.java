package net.lwenstrom.musicquizbackend.config;

import net.lwenstrom.musicquizbackend.MyWebSocketHandler;
import net.lwenstrom.musicquizbackend.PlaylistParser;
import net.lwenstrom.musicquizbackend.repo.PlaylistRepository;
import net.lwenstrom.musicquizbackend.repo.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {
    @Autowired
    SongRepository songRepository;
    @Autowired
    PlaylistRepository playlistRepository;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        PlaylistParser parser = new PlaylistParser(playlistRepository);
        MyWebSocketHandler webSocketHandler = new MyWebSocketHandler(songRepository);
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/")
                .setAllowedOrigins("*");

    }
}
