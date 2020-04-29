package jacobs.tycoon.clientcontroller

import jacobs.tycoon.controller.communication.SimpleRequest
import jacobs.tycoon.controller.communication.SimpleRequestWrapper
import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.pieces.PlayingPieceList
import jacobs.websockets.content.BooleanContent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

// TODO: Look at adding redraw hook to mithril directly
@Suppress( "UNCHECKED_CAST" )
class OutgoingRequestController(kodein: Kodein ) {

    private val network by kodein.instance < Network > ()

    fun addPlayer( name: String, playingPiece: PlayingPiece ) {
        TODO("Not yet implemented")
    }

    fun getAvailablePiecesAsync(): Deferred < PlayingPieceList > {
        return this.network.sendRequestAsync(
            SimpleRequestWrapper( SimpleRequest.AVAILABLE_PIECES )
        ) as Deferred < PlayingPieceList >
    }

    fun getGameStage(): GameStage {
        TODO("Not yet implemented")
    }

}