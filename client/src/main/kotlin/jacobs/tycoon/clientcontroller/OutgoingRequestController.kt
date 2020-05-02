package jacobs.tycoon.clientcontroller

import jacobs.tycoon.controller.communication.AddPlayerRequest
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.SimpleRequest
import jacobs.tycoon.controller.communication.SimpleRequestWrapper
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.websockets.content.BooleanContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class OutgoingRequestController( kodein: Kodein ) {

    private val network by kodein.instance < Network > ()

    suspend fun addPlayer( name: String, playingPiece: PlayingPiece ): Boolean {
        return this.sendYesNoRequest( AddPlayerRequest( name, playingPiece ) )
    }

    suspend fun startGame(): Boolean {
        return this.sendSimpleYesNoRequest( SimpleRequest.START_GAME )
    }

    private suspend fun sendSimpleYesNoRequest( requestType: SimpleRequest ): Boolean {
        return this.sendYesNoRequest( SimpleRequestWrapper( requestType ) )
    }

    private suspend fun sendYesNoRequest( request: Request ): Boolean {
        return this.network.sendRequest( request )
            .let { response -> ( response as BooleanContent ).boolean }
    }

}