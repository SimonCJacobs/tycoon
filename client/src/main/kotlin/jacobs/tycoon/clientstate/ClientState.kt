package jacobs.tycoon.clientstate

import jacobs.websockets.SocketId

class ClientState {
    var isWaitingForServer = true
    var socket: SocketId = SocketId.NULL
}