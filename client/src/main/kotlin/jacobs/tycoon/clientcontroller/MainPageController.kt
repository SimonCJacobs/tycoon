package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainPageController( kodein: Kodein ): UserInterfaceController( kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun canGameStart(): Boolean {
        return this.gameState.game().canGameStart()
    }

    fun getOwnPlayer(): Player {
        return this.gameState.game().players.getPlayerAtPosition( this.clientState.socket.toPosition() )
    }

    fun startGame() {
        launch {
            if ( canGameStart() )
                outgoingRequestController.completeGameSignUp()
            // TODO deal with client or server saying no!!!
        }
    }

}