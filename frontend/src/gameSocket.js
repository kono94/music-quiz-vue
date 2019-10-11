import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import * as SocketRoutes from './socket-routes';

class GameSocket {
  constructor() {
    this.socket = null;
    this.uri = 'http://localhost:8081/';
  }

  async connectToSocket(store) {
    console.warn('Connect to socket...');
    if (this.socket && this.socket.readyState === this.socket.OPEN) {
      console.warn('Reuse existing socket!');
      return;
    }

    this.store = store;
    console.warn('Creating new connectionInfo!');
    const sock = new SockJS(this.uri);
    this.socket = Stomp.over(sock);
    await new Promise(((resolve, reject) => this.socket.connect({}, resolve, reject)));
    await this.changeUserName(this.store.getters.username);
  }

  isOpen() {
    console.log(this.socket)
    return this.socket !== null && this.socket.connected;
  }

  closeSocket() {
    this.socket.close();
  }

  send(route, content) {
    this.socket.send(route, JSON.stringify(content), {});
  }

  setReadyState(state) {
    this.send(SocketRoutes.SET_READY_STATE, state);
  }

  joinRoom() {
    this.send(SocketRoutes.JOIN_ROOM, {
      roomID: this.store.getters.roomID,
    });
  }

  leaveRoom() {
    this.send(SocketRoutes.LEAVE_ROOM, {
      roomID: this.store.getters.roomID,
    });
  }

  guessSong(artistGuess, songGuess) {
    this.send(SocketRoutes.GUESS_SONG, {
      artistGuess,
      songGuess,
    });
  }

  requestLobbyData() {
    this.send(SocketRoutes.REQUEST_LOBBY_LIST_UPDATE, {});
  }

  changeUserName(username) {
    this.send(SocketRoutes.CHANGE_USERNAME, {
      username,
    });
  }

  subscribe(route, fn) {
    this.socket.subscribe(route, (message) => {
      fn(JSON.parse(message.body));
    });
  }
}
const gameSocket = new GameSocket();
export default gameSocket;
