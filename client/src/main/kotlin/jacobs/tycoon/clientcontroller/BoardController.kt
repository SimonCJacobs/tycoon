package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.cards.Card
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.BankruptcyProceedings
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.CrownTheVictor
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class BoardController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun board(): Board {
        return this.gameState.game().board
    }

    fun carryOutBankruptcy() {
        launch { outgoingRequestController.carryOutBankruptcy() }
    }

    fun getBankruptPlayer(): Player {
        return gameState.game().getBankruptPlayer()
    }

    fun getCardBeingRead(): Card {
        return this.gameState.game().getCardBeingRead()
    }

    fun getTheWinner(): Player {
        return this.gameState.game().getTheWinner()
    }

    fun isAuctionUnderway(): Boolean {
        return gameState.game().isPhase < AuctionProperty > ()
    }

    fun isBankruptcyUnderway(): Boolean {
        return gameState.game().isPhase < BankruptcyProceedings > ()
    }

    fun isReadingCard(): Boolean {
        return this.gameState.game().anyPastPhasesOfTypeInTurn < CardReading > ()
    }

    fun isThereAWinner(): Boolean {
        return this.gameState.game().isPhase < CrownTheVictor > ()
    }

    fun isTradeBeingConsidered(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse {
            gameState.game().isTradeBeingConsideredBy( it )
        }
    }

    fun isTradeBeingConsideredBySomeoneElse(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse {
            gameState.game().isTradeBeingConsideredBySomeoneOtherThan( it )
        }
    }

}