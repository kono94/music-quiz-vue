package net.lwenstrom.musicquizbackend.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.lwenstrom.musicquizbackend.model.Player;
import org.springframework.web.socket.WebSocketSession;


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
    }
    public void removePlayer(WebSocketSession session){
        if(session != null && players.get(session) != null){
            players.get(session).setRoomID(null);
            players.remove(session);
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
    public Map<WebSocketSession, Player> getPlayersMap(){
        return players;
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
