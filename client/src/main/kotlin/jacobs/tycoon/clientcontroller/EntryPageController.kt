package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.clientstate.EntryPageState
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

open class EntryPageController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    protected val clientState by kodein.instance < ClientState > ()
    protected val entryPageState by kodein.instance < EntryPageState > ()
    protected val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    private val gameState by kodein.instance < GameState > ()

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.gameState.game().getAvailablePieces()
    }

    fun getSelectedPiece( availablePieces: List < PlayingPiece > ): PlayingPiece {
        if ( null == entryPageState.selectedPiece )
            return availablePieces.random()
        else
            return entryPageState.selectedPiece!!
    }

    open fun onEntryPageButtonClick() {
        when {
            this.gameState.game().canAnyNewPlayerJoin() ->
                this.runServerAddPlayerProcess( entryPageState.playerNameInProgress, entryPageState.selectedPiece!! )
            else -> this.entryPageState.showNoGameEntry = true
        }
    }

    private fun runServerAddPlayerProcess( playerName: String, playingPiece: PlayingPiece ) {
        launch {
            outgoingRequestController.addPlayer( playerName, playingPiece )
                .let {
                    if ( it.isUnassigned() )
                        entryPageState.showNoGameEntry = true
                    else
                        clientState.seatingPosition = it
                }
        }
    }

}