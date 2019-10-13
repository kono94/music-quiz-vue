package net.lwenstrom.musicquizbackend.config;

import net.lwenstrom.musicquizbackend.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        MyWebSocketHandler webSocketHandler = new MyWebSocketHandler();
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/")
                .setAllowedOrigins("*");

    }
}
