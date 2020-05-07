package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.websockets.SocketId

fun SocketId.toPosition(): SeatingPosition {
    return SeatingPosition( this.index )
}