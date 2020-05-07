package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.EntryPageState
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class EntryPageController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val entryPageState by kodein.instance < EntryPageState > ()
    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.gameState.game().getAvailablePieces()
    }

    fun getSelectedPiece( availablePieces: List < PlayingPiece > ): PlayingPiece {
        if ( null == entryPageState.selectedPiece )
            return availablePieces.random()
        else
            return entryPageState.selectedPiece!!
    }

    fun onEntryPageButtonClick() {
        if ( this.gameState.game().canNewPlayerJoin() )
            this.runServerAddPlayerProcess( entryPageState.playerNameInProgress, entryPageState.selectedPiece!! )
        else
            this.entryPageState.showNoGameEntry = true
    }

    private fun runServerAddPlayerProcess( playerName: String, playingPiece: PlayingPiece ) {
        launch {
            val addResult = outgoingRequestController.addPlayer( playerName, playingPiece )
            if ( false == addResult )
                entryPageState.showNoGameEntry = true
        }
    }

}