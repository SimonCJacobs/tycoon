package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.OpenActionRequest
import jacobs.tycoon.controller.communication.PositionalActionRequest
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.OpenGameAction
import jacobs.tycoon.domain.actions.PositionalGameAction
import jacobs.tycoon.domain.actions.RollTheDice
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.services.Network
import jacobs.websockets.content.BooleanContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class OutgoingRequestController( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val network by kodein.instance <Network> ()

    suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): Boolean {
        return this.sendPositionalActionRequest( AddPlayer( playerName, playingPiece ) )
    }

    suspend fun completeGameSignUp(): Boolean {
        return this.sendOpenActionRequest( CompleteSignUp() )
    }

    suspend fun rollTheDice(): Boolean {
        return this.sendPositionalActionRequest( RollTheDice() )
    }

    private suspend fun sendOpenActionRequest( openAction: OpenGameAction ): Boolean {
        return this.sendYesNoRequest( OpenActionRequest( openAction ) )
    }

    private suspend fun sendPositionalActionRequest( positionalAction: PositionalGameAction ): Boolean {
        return this.sendYesNoRequest( PositionalActionRequest( positionalAction ) )
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