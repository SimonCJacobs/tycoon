package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.view.ViewState
import jacobs.tycoon.clientstate.EntryPageState
import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.SignUp
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class UserInterfaceController( kodein: Kodein ) : CoroutineScope by kodein.direct.instance() {

    private val clientState by kodein.instance < ClientState > ()
    private val entryPageState by kodein.instance < EntryPageState > ()
    private val game by kodein.instance < Game > ()
    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun canGameStart(): Boolean {
        return this.game.canGameStart( gameState )
    }

    fun getAvailablePieces(): List < PlayingPiece > {
        return game.getAvailablePieces( gameState.pieceSet, gameState.players )
    }

    fun getPlayerCount(): Int {
        return this.gameState.players.count()
    }

    fun getSelectedPiece( availablePieces: List < PlayingPiece > ): PlayingPiece {
        if ( null == entryPageState.selectedPiece )
            return availablePieces.random()
        else
            return entryPageState.selectedPiece!!
    }

    fun getViewStage(): ViewState {
        return this.clientState.viewState
    }

    fun isAppWaitingForServer(): Boolean {
        return this.clientState.isWaitingForServer
    }

    fun isSignUpPhase(): Boolean {
        return this.gameState.phase == SignUp()
    }

    fun onEntryPageButtonClick() {
        if ( false == this.isAddPlayerValidOnClientSide() )
            TODO( "Not implemented new player client-side validation yet, or dealing with bad entry" )
        launch {
            val addResult = outgoingRequestController.addPlayer(
                entryPageState.playerName, entryPageState.selectedPiece!!
            )
            if ( false == addResult )
                throw Error( "Not dealt with this yet!" ) //TODO player validation
            goToPlayingAreaView()
        }
    }

    fun startGame() {
        launch {
            if ( canGameStart() )
                outgoingRequestController.startGame()
            // TODO deal with client or server saying no!!!
        }
    }

    private fun goToPlayingAreaView() {
        this.clientState.viewState = ViewState.PLAYING_AREA
    }

    private fun isAddPlayerValidOnClientSide(): Boolean {
        // TODO ("Not yet implemented")
        return true
    }

}