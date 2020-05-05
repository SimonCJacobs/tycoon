package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.ActionRequest
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.RollForOrder
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.services.Network
import jacobs.websockets.content.BooleanContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class OutgoingRequestController( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val network by kodein.instance <Network> ()

    suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): Boolean {
        return this.sendActionRequest( AddPlayer( playerName, playingPiece ) )
    }

    suspend fun completeGameSignUp(): Boolean {
        return this.sendActionRequest( CompleteSignUp() )
    }

    suspend fun rollTheDice(): Boolean {
        return this.sendActionRequest( RollForOrder() )
    }

    private suspend fun sendActionRequest( action: GameAction ): Boolean {
        return this.sendYesNoRequest( ActionRequest( action ) )
    }

    private suspend fun sendYesNoRequest( request: Request ): Boolean {
        this.clientState.isWaitingForServer = true
        return this.sendRequestOnNetwork( request )
    }

    private suspend fun sendRequestOnNetwork( request: Request ): Boolean {
        return this.network.sendRequest( request )
            .let { response -> ( response as BooleanContent ).boolean }
    }

}