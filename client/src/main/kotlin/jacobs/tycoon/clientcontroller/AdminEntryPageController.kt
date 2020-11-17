package jacobs.tycoon.clientcontroller

import jacobs.mithril.Mithril
import kotlinx.coroutines.launch
import org.kodein.di.Kodein

class AdminEntryPageController( kodein: Kodein ) : EntryPageController( kodein ) {

    private val mithril = Mithril()

    override fun onEntryPageButtonClick() {
        this.accessAdminScreenIfUsernameCorrect( entryPageState.playerNameInProgress )
    }

    private fun accessAdminScreenIfUsernameCorrect( username: String ) {
        launch {
            if ( outgoingRequestController.makeAdminEntryRequest( username ) ) {
                clientState.authorisedToAdministrate = true
                clientState.isWaitingForServer = false
                mithril.redraw()
            }
        }
    }

}