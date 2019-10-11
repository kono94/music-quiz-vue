import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

export default class GameSocket extends EventTarget {
  constructor(store) {
    super();
    this.socket = null;
    this.store = store;
  }

  async connectToSocket(uri) {
    console.warn('Connect to socket...');

    if (this.socket && this.socket.readyState === this.socket.OPEN) {
      console.warn('Reuse existing socket!');
      return;
    }

    console.warn('Creating new connectionInfo!');
    const sock = new SockJS(uri);
    this.socket = Stomp.over(sock);
    this.socket.connect();
    this.socket.addEventListener('open', e => this.dispatchEvent(new CustomEvent('open', e)));

    this.socket.addEventListener('message', e => this.dispatchEvent(new CustomEvent('message', {
      detail: JSON.parse(e.data),
    })));
    this.socket.addEventListener('error', e => this.dispatchEvent(new CustomEvent('error', e)));
    this.socket.addEventListener('close', e => this.dispatchEvent(new CustomEvent('close', e)));
  }

  isOpen() {
    return this.socket.readyState === this.socket.OPEN;
  }

  closeSocket() {
    this.socket.close();
  }

  async registerUser() {
    await this.socket.send(JSON.stringify({
      type: 'REGISTER_AS_PLAYER',
      userAction: {
        userName: this.store.getters.userName,
        data: {
          paddleColor: this.store.getters.userColor,
        },
      },
    }));
  }

  async sendReadyState(ready, config) {
    await this.socket.send(JSON.stringify({
      type: 'SET_READY_STATE',
      userAction: {
        data: {
          ready,
          ...config,
        },
      },
    }));
  }

  async fetchGameState() {
    await this.socket.send(JSON.stringify({ type: 'GET_GAME_STATE' }));
  }

  async fetchUserList() {
    await this.socket.send(JSON.stringify({ type: 'GET_LOBBY_INFO' }));
  }

  async setUserAction(userAction) {
    await this.socket.send(JSON.stringify({ type: 'SET_PADDLE_ACTION', userAction }));
  }

  async sendCustomPaddle(paddleCoords) {
    await this.socket.send(JSON.stringify({
      type: 'SET_CUSTOM_PADDLE',
      userAction: {
        data: {
          coords: paddleCoords,
        },
      },
    }));
  }
}
