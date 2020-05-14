package jacobs.tycoon.clientcontroller

import jacobs.mithril.Mithril
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.GameController
import jacobs.tycoon.state.GameHistory
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.actions.GameAction
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class StateSynchroniser ( kodein: Kodein ) {

    private val changeListener by kodein.instance < ChangeListener >()
    private val clientState by kodein.instance < ClientState >()
    private val gameController by kodein.instance < GameController > ()
    private val gameHistory by kodein.instance < GameHistory >()
    private val mithril = Mithril()

    suspend fun applyUpdates( stateAction: GameActionCollection ) {
        stateAction.getActions().forEach {
            applySingleAction( it )
        }
        this.clientState.isWaitingForServer = false
        mithril.redraw()
    }

    private suspend fun applySingleAction( gameAction: GameAction ) {
        this.gameController.duplicate( gameAction )
        changeListener.onAction( gameAction )
        gameHistory.logAction( gameAction )
    }

}