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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Controller
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    /*
     * Maps roomID to room. Rooms themselves have the roomID itself, but this map
     * allows non-iterative access to the rooms (useful for joining rooms)
     */
    private Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    /*
     * All connected session will have a "Player"-Object connected to them.
     * GameRooms will have a "subset" of this Map to save players in room and update
     * all connected players to this specific room
     */
    private Map<WebSocketSession, Player> sessionToPlayer = new ConcurrentHashMap<>();
    /*
      Quickly access the room the player is currently in instead of iterating through all rooms
     */
    private Map<Player, GameRoom> playerToRoom = new ConcurrentHashMap<>();
    /*
     * Sessions that are not in a room yet and receive updates about the current states of every room
     * to sync the lobby list.
     * No need for players in rooms to receive those messages as well
     */
    private Set<WebSocketSession> sessionsRoomless = new CopyOnWriteArraySet<>();

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
        player.setSessionID(session.getId());
        sessionToPlayer.put(session, player);
        sessionsRoomless.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        MessageWrapper messageWrapper = parseMessage(message, MessageWrapper.class);
        Player player = sessionToPlayer.get(session);
        GameRoom playerRoom = playerToRoom.get(player);

        switch (messageWrapper.getEvent()) {
            case CHANGE_USERNAME:
                ChangeUsernamePayload cm = parsePayload(messageWrapper, ChangeUsernamePayload.class);
                if (cm.getUsername() != null) {
                    player.setUsername(cm.getUsername());
                }
                send(session, Event.REFRESH_PLAYER, player);
                if(playerRoom != null){
                    updatePlayersInRoom(playerRoom);
                }
                break;
            case TOGGLE_READY:
                if(playerRoom != null){
                   player.setReady(!player.isReady());
                   send(session, Event.REFRESH_PLAYER, player);
                   updatePlayersInRoom(playerRoom);
                }
                break;
            case REQUEST_ROOM_UPDATE:
                if(playerRoom != null){
                    send(session, Event.REFRESH_ROOM, playerRoom);
                }
                break;
            case REQUEST_LOBBY_LIST_UPDATE:
                send(session, Event.REFRESH_LOBBY_LIST, rooms.values());
                break;
            case JOIN_ROOM:
                JoinRoomPayload jr = parsePayload(messageWrapper, JoinRoomPayload.class);
                if(rooms.containsKey(jr.getRoomID())){
                    GameRoom roomToJoin = rooms.get(jr.getRoomID());
                    if(playerRoom != null){
                        if(playerRoom.removePlayer(session)){
                            updatePlayersInRoom(playerRoom);
                        }
                    }
                    if(roomToJoin.addPlayer(session, player)){
                        playerToRoom.put(player, roomToJoin);
                        updatePlayersInRoom(roomToJoin);
                        sessionsRoomless.remove(session);
                        send(sessionsRoomless, Event.REFRESH_LOBBY_LIST, rooms.values());
                    }
                }
                break;
            case LEAVE_ROOM:
                if(playerRoom != null){
                    if(playerRoom.removePlayer(session)){
                        playerToRoom.remove(player);
                        send(session, Event.REFRESH_PLAYER, player);
                        send(session, Event.REFRESH_ROOM, null);
                        updatePlayersInRoom(playerRoom);
                        sessionsRoomless.add(session);
                        send(sessionsRoomless, Event.REFRESH_LOBBY_LIST, rooms.values());
                    }
                }
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        Player player = sessionToPlayer.get(session);
        GameRoom playerRoom = playerToRoom.get(player);
        if(playerRoom != null){
            playerRoom.removePlayer(session);
            updatePlayersInRoom(playerRoom);
        }
        sessionToPlayer.remove(session);
        sessionsRoomless.remove(session);
        playerToRoom.remove(player);
        send(sessionToPlayer.keySet(), Event.REFRESH_LOBBY_LIST, rooms.values());
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

    private void updatePlayersInRoom(GameRoom gameRoom) throws IOException {
        send(gameRoom.getPlayersMap().keySet(), Event.REFRESH_ROOM, gameRoom);
    }
    private <T> void send(Set<WebSocketSession> sessions, Event event, T payload) throws IOException {
        for(WebSocketSession session: sessions){
            send(session, event, payload);
        }
    }
}
