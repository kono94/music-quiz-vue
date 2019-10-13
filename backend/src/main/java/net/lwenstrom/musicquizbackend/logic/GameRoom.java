package net.lwenstrom.musicquizbackend.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.lwenstrom.musicquizbackend.model.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GameRoom {
    private String id;
    private boolean isRunning;
    private Set<Player> players;
    @JsonIgnore
    private boolean hidden;

    public GameRoom(String id){
        this.id = id;
        isRunning = false;
        players = new CopyOnWriteArraySet<>();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
