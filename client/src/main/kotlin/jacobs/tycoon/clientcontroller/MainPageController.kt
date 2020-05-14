package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.services.PlayerIdentifier
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainPageController( kodein: Kodein ): UserInterfaceController( kodein ) {

    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()
    private val playerIdentifier by kodein.instance < PlayerIdentifier > ()

    fun canGameStart(): Boolean {
        return this.gameState.game().canGameStart()
    }

    fun getOwnPlayer(): Player {
        return playerIdentifier.playerUsingThisMachine
    }

    fun startGame() {
        launch {
            if ( canGameStart() )
                outgoingRequestController.completeGameSignUp()
            // TODO deal with client or server saying no!!!
        }
    }

}