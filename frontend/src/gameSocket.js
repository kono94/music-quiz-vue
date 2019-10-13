import * as SocketRoutes from './events';
import { SET_ROOM, SET_SOCKET_STATE, SET_USER } from './mutation-types';

class GameSocket {
  constructor() {
    this.socket = null;
    this.uri = 'ws://localhost:8081/';
  }

  async connectToSocket(store) {
    console.log('Connect to socket...');
    if (this.socket && this.socket.readyState === this.socket.OPEN) {
      console.log('Reuse existing socket!');
      return;
    }
    this.store = store;
    console.log('Creating new connectionInfo!');

    await new Promise(((resolve, reject) => {
      this.socket = new WebSocket(this.uri);

      this.socket.onopen = () => {
        console.log('webSocket opened');
        this.store.commit(SET_SOCKET_STATE, {
          isConnected: true,
        });
        resolve();
      };

      this.socket.onclose = () => {
        console.log('webSocket closed');
        this.store.commit(SET_SOCKET_STATE, {
          isConnected: false,
        });
      };

      this.socket.onerror = (error) => {
        reject(error);
      };

      this.socket.addEventListener('message', (event) => {
        const msg = JSON.parse(event.data);
        console.log(msg);
        switch (msg.event) {
          case SocketRoutes.REFRESH_ROOM:
            this.store.commit(SET_ROOM, msg.payload);
            break;
          case SocketRoutes.REFRESH_PLAYER:
            this.store.commit(SET_USER, msg.payload);
            break;
        }
      });
    }));


    await this.changeUserName(this.store.getters.username);
  }

  send(route, content) {
    console.log(JSON.stringify({
      event: route,
      payload: content,
    }));

    this.socket.send(JSON.stringify({
      event: route,
      payload: JSON.stringify(content),
    }));
  }

  setReadyState(state) {
    this.send(SocketRoutes.SET_READY_STATE, state);
  }

  joinRoom(roomID) {
    this.send(SocketRoutes.JOIN_ROOM, {
      roomID,
    });
  }

  leaveRoom() {
    this.send(SocketRoutes.LEAVE_ROOM, {});
  }

  guessSong(artistGuess, songGuess) {
    this.send(SocketRoutes.GUESS_SONG, {
      artistGuess,
      songGuess,
    });
  }

  toggleReady(){
    this.send(SocketRoutes.TOGGLE_READY, {});
  }

  requestLobbyListUpdate() {
    this.send(SocketRoutes.REQUEST_LOBBY_LIST_UPDATE, {});
  }

  requestRoomData(){
    this.send(SocketRoutes.REQUEST_ROOM_UPDATE, {});
  }

  changeUserName(username) {
    this.send(SocketRoutes.CHANGE_USERNAME, {
      username,
    });
  }

  addEventListener(type, fn) {
    this.socket.addEventListener(type, fn);
  }

  removeEventListener(type, fn) {
    this.socket.removeEventListener(type, fn);
  }
}
const gameSocket = new GameSocket();
export default gameSocket;
