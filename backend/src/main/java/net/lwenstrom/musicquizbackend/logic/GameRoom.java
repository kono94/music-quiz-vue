package net.lwenstrom.musicquizbackend.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.lwenstrom.musicquizbackend.model.Player;
import org.springframework.web.socket.WebSocketSession;


import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom implements Runnable{
    private String id;
    private boolean isRunning;
    private Map<WebSocketSession, Player> players;
    private String adminSessionID;

    public GameRoom(String id){
        this.id = id;
        isRunning = false;
        players = new ConcurrentHashMap<>();
        adminSessionID = null;
    }

    public boolean addPlayer(WebSocketSession session, Player player){
        if(players.size() == 0){
            adminSessionID = player.getSessionID();
        }
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

    @Override
    public void run() {
        while (true){
            checkRunning();
            System.out.println("main loop");
        }
    }

    private synchronized void checkRunning(){
        while(!isRunning) {
            try {
                System.out.println("sleeping");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void stopGame(Player player){
        if(adminSessionID != null && adminSessionID.equals(player.getSessionID())){
            stopGame();
        }
    }

    private synchronized void stopGame(){
        isRunning = false;
        for(Player player : players.values()){
            player.setReady(false);
        }
        notifyAll();
    }

    public synchronized void startGame(Player player){
        System.out.println(adminSessionID);
        System.out.println(player.getSessionID());
        if(adminSessionID != null && adminSessionID.equals(player.getSessionID())){
            isRunning = true;
            System.out.println("run");
            notifyAll();
        }else{
            // throw exception not admin
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
    @JsonIgnore
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

    public String getAdminSessionID() {
        return adminSessionID;
    }

    public void setAdminSessionID(String adminSessionID) {
        this.adminSessionID = adminSessionID;
    }

}
