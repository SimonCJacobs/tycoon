package jacobs.tycoon.clientcontroller

import jacobs.tycoon.controller.communication.AddPlayerRequest
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
class OutgoingRequestController( kodein: Kodein ) {

    private val network by kodein.instance < Network > ()

    suspend fun addPlayer( name: String, playingPiece: PlayingPiece ): Boolean {
        return this.network.sendRequest( AddPlayerRequest( name, playingPiece ) )
            .let { response -> ( response as BooleanContent ).boolean }
    }

    suspend fun getAvailablePieces(): PlayingPieceList {
        return this.network.sendRequest(
            SimpleRequestWrapper( SimpleRequest.AVAILABLE_PIECES )
        ) as PlayingPieceList
    }

    suspend fun getGameStage(): GameStage {
        TODO("Not yet implemented")
    }

}