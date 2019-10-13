package net.lwenstrom.musicquizbackend;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.lwenstrom.musicquizbackend.logic.GameRoom;
import net.lwenstrom.musicquizbackend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    private Map<String, GameRoom> rooms = new HashMap<>();
    private Map<WebSocketSession, Player> sessionToPlayer = new HashMap<>();

    public MyWebSocketHandler() {
        for(int i = 0; i<5; ++i){
            GameRoom room = new GameRoom(Integer.toString(i));
            rooms.put(room.getId(), room);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("new connection");
        Player player = new Player();
        player.setUsername("noName");
        sessionToPlayer.put(session, player);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        MessageWrapper messageWrapper = parseMessage(message, MessageWrapper.class);

        switch (messageWrapper.getEvent()) {
            case CHANGE_USERNAME:
                ChangeUsernamePayload cm = parsePayload(messageWrapper, ChangeUsernamePayload.class);
                if (cm.getUsername() != null) {
                    sessionToPlayer.get(session).setUsername(cm.getUsername());
                }
            case REQUEST_LOBBY_LIST_UPDATE:
                send(session, Event.REFRESH_LOBBY_LIST, rooms.values());
                break;
            case JOIN_ROOM:
                JoinRoomPayload jr = parsePayload(messageWrapper, JoinRoomPayload.class);
                if(rooms.containsKey(jr.getRoomID())){
                    rooms.get(jr.getRoomID()).getPlayers().add(sessionToPlayer.get(session));
                    send(session, Event.REFRESH_ROOM, rooms.get(jr.getRoomID()));
                }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Player player = sessionToPlayer.get(session);
        if(player.getRoomID() != null && rooms.containsKey(player.getRoomID())){
            rooms.get(player.getRoomID()).getPlayers().remove(player);
            player.setRoomID(null);
        }
        sessionToPlayer.remove(session);
    }

    private <T> T parseMessage(TextMessage textMessage, Class<T> classToParse) throws IOException {
        return new ObjectMapper().readValue(textMessage.getPayload(), classToParse);
    }

    private <T> T parsePayload(MessageWrapper messageWrapper, Class<T> classToParse) throws IOException {
        return new ObjectMapper().readValue(messageWrapper.getPayload().toString(), classToParse);
    }

    private <T> void send(WebSocketSession session, Event event, T payload) throws IOException {
        MessageWrapper lobbyList = new MessageWrapper<HashMap>();
        lobbyList.setEvent(event);
        lobbyList.setPayload(payload);
        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(lobbyList)));
    }
}
