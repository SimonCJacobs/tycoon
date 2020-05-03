package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.view.ViewState
import jacobs.tycoon.clientstate.EntryPageState
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
    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun canGameStart(): Boolean {
        return this.gameState.game.canGameStart()
    }

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.gameState.game.getAvailablePieces( gameState.game.pieceSet, gameState.game.players )
    }

    fun getPlayerCount(): Int {
        return this.gameState.game.players.count()
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
        return this.gameState.game.isSignUpPhase()
    }

    fun onEntryPageButtonClick() {
        if ( this.gameState.game.canNewPlayerJoin() )
            this.runServerAddPlayerProcess()
        else
            this.entryPageState.showNoGameEntry = true
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

    private fun runServerAddPlayerProcess() {
        launch {
            val addResult = outgoingRequestController.addPlayer(
                entryPageState.playerName, entryPageState.selectedPiece!!
            )
            if ( false == addResult )
                entryPageState.showNoGameEntry = true
            else
                goToPlayingAreaView()
        }
    }

}