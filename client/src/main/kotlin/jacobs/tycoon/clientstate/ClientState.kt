package jacobs.tycoon.clientstate

import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.websockets.SocketId

class ClientState {
    var isWaitingForServer = true
    var pieceInDrag: PlayingPiece? = null
    var socket: SocketId = SocketId.NULL
}