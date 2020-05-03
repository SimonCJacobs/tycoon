package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.players.Position
import jacobs.websockets.SocketId

fun SocketId.toPosition(): Position {
    return Position( this.index )
}