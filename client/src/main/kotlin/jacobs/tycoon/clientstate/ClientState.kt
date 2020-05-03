package jacobs.tycoon.clientstate

import jacobs.websockets.SocketId

class ClientState {
    var isWaitingForServer = true
    lateinit var socket: SocketId
}