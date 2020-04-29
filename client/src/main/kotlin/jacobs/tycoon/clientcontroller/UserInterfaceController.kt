package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.pieces.PlayingPieceList
import jacobs.tycoon.view.ViewState
import jacobs.tycoon.view.components.pages.EntryPageState
import kotlinx.coroutines.Deferred
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class UserInterfaceController( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun getAvailablePiecesAsync(): Deferred < PlayingPieceList > {
        return this.outgoingRequestController.getAvailablePiecesAsync()
    }

    fun getViewStage(): ViewState {
        return this.clientState.viewState
    }

    fun onEntryPageButtonClick( entryPageState: EntryPageState ) {
        this.outgoingRequestController.addPlayer( entryPageState.playerName, entryPageState.selectedPiece )
        this.goToPlayingAreaView()
    }

    private fun goToPlayingAreaView() {
        this.clientState.viewState = ViewState.PLAYING_AREA
    }

}