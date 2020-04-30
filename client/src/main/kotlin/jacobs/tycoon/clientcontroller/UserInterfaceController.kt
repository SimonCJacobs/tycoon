package jacobs.tycoon.clientcontroller

import jacobs.mithril.Mithril
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.pieces.PlayingPieceList
import jacobs.tycoon.view.ViewState
import jacobs.tycoon.view.components.pages.EntryPageState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class UserInterfaceController( kodein: Kodein ) : CoroutineScope by kodein.direct.instance() {

    private val clientState by kodein.instance < ClientState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun getAvailablePiecesAsync(): Deferred < PlayingPieceList > {
        return async { outgoingRequestController.getAvailablePieces() }
    }

    fun getViewStage(): ViewState {
        return this.clientState.viewState
    }

    fun onEntryPageButtonClick( entryPageState: EntryPageState ) {
        entryPageState.isReady = false
        launch {
            val addResult = outgoingRequestController.addPlayer( entryPageState.playerName, entryPageState.selectedPiece )
            if ( false == addResult )
                throw Error( "Not dealt with this yet!" ) //TODO player validation
            goToPlayingAreaView()
            Mithril().redraw()
        }
    }

    private fun goToPlayingAreaView() {
        this.clientState.viewState = ViewState.PLAYING_AREA
    }

}