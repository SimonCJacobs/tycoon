package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.cards.Card
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class BoardController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val gameState by kodein.instance < GameState > ()

    fun board(): Board {
        return this.gameState.game().board
    }

    fun getCardBeingRead(): Card {
        return this.gameState.game().getCardBeingRead()
    }

    fun isReadingCard(): Boolean {
        return this.gameState.game().anyPastPhasesOfTypeInTurn < CardReading > ()
    }

}