package jacobs.tycoon.clientstate

import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.websockets.SocketId

class ClientState {
    var isPlayerComposingDeal: Boolean = false
    var isWaitingForServer = true
    var maybeSocket: SocketId? = null
    var pieceBeingDragged: PlayingPiece? = null
    val socketNotNull: SocketId
        get() = maybeSocket!!
}