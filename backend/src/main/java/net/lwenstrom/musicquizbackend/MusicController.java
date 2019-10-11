package net.lwenstrom.musicquizbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MusicController {
    private static final Logger logger = LoggerFactory.getLogger(MusicController.class);
    private Map<String, String> gameRooms = new HashMap<>();

    @Autowired
    private SimpMessageSendingOperations simpMessagingTemplate;

    @MessageMapping("/music/REQUEST_LOBBY_LIST_UPDATE")
    public void refreshLobbyListData(){
        System.out.println("kek");
        gameRooms.put("1","23");
        simpMessagingTemplate.convertAndSend("/music/" + SocketRoutes.REFRESH_LOBBY_LIST, gameRooms);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("User: {} disconnected from room:", event.getSessionId());
    }
}
