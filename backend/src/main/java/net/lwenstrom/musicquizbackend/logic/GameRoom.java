package net.lwenstrom.musicquizbackend.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.lwenstrom.musicquizbackend.MyWebSocketHandler;
import net.lwenstrom.musicquizbackend.model.Event;
import net.lwenstrom.musicquizbackend.model.Player;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom {
    private String id;
    private boolean isRunning;
    private Map<WebSocketSession, Player> players;
    @JsonIgnore
    private boolean hidden;

    public GameRoom(String id){
        this.id = id;
        isRunning = false;
        players = new ConcurrentHashMap<>();
    }

    public void addPlayer(WebSocketSession session, Player player){
        player.setRoomID(id);
        players.put(session, player);
        refreshRoomForAllPlayers();
    }
    public void removePlayer(WebSocketSession session){
        players.get(session).setRoomID(null);
        players.remove(session);
        refreshRoomForAllPlayers();
    }

    public void refreshRoomForAllPlayers(){
        try {
            MyWebSocketHandler.send(players.keySet(), Event.REFRESH_ROOM, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public void setPlayers(Map<WebSocketSession, Player> players) {
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
