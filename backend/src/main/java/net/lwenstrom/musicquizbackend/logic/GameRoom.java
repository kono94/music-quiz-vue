package net.lwenstrom.musicquizbackend.logic;

import net.lwenstrom.musicquizbackend.model.Player;
import org.springframework.web.socket.WebSocketSession;


import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom {
    private String id;
    private boolean isRunning;
    private Map<WebSocketSession, Player> players;

    public GameRoom(String id){
        this.id = id;
        isRunning = false;
        players = new ConcurrentHashMap<>();
    }

    public boolean addPlayer(WebSocketSession session, Player player){
        player.setReady(false);
        players.put(session, player);
        return true;
    }

    public boolean removePlayer(WebSocketSession session){
        if(session != null){
          Player player = players.get(session);
          if(player != null){
              player.setReady(false);
              players.remove(session);
              return true;
          }
        }
        return false;
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
