package jacobs.tycoon.clientcontroller

import jacobs.tycoon.controller.communication.AddPlayerRequest
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.PlayerCall
import jacobs.websockets.content.BooleanContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class OutgoingRequestController( kodein: Kodein ) {

    private val network by kodein.instance < Network > ()

    suspend fun addPlayer( name: String, playingPiece: PlayingPiece ): Boolean {
        return this.network.sendRequest( AddPlayerRequest( name, playingPiece ) )
            .let { response -> ( response as BooleanContent ).boolean }
    }

}