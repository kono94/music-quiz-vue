package net.lwenstrom.musicquizbackend.model;

public enum Event {
    JOIN_ROOM, LEAVE_ROOM, REFRESH_LOBBY_LIST,
    CHANGE_USERNAME,SET_READY_STATE, GUESS_SONG,
    SONG_CHANGED, REQUEST_INGAME_ROOM_UPDATE,
    REQUEST_LOBBY_ROOM_UPDATE, REFRESH_INGAME_ROOM_INFO,
    REFRESH_LOBBY_ROOM_INFO, REQUEST_LOBBY_LIST_UPDATE, REFRESH_ROOM, REFRESH_PLAYER,
    REQUEST_ROOM_UPDATE, TOGGLE_READY, START_GAME, STOP_GAME,
}
