package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.JailRules

class RollingForMoveFromJail(
    playerWithTurn: Player, jailRules: JailRules
) : RollingForMove( playerWithTurn, jailRules ) {

    private var withGetOutOfJailFreeCard = false

    override fun decideResultOfDiceRoll( game: Game ): RollForMoveResult {
        return when {
            withGetOutOfJailFreeCard -> super.decideResultOfDiceRoll( game )
            diceRoll.isDouble() -> super.decideResultOfDiceRoll( game )
            playerWithTurn.howManyRollsHadOnCurrentSquare() == jailRules.mustLeaveRollCount
                -> this.payFineAndLeaveJail( game )
            else -> this.remainInJail()
        }
    }

    fun usingGetOutOfJailFreeCard() {
        withGetOutOfJailFreeCard = true
    }

    private fun payFineAndLeaveJail( game: Game ): RollForMoveResult {
        if ( playerWithTurn.cashHoldings < jailRules.leaveJailFineAmount )
            return this.commenceBankruptcyProceedings()
        playerWithTurn.debitFunds( jailRules.leaveJailFineAmount )
        return this.waitForPlayerToMove( game )
    }

    private fun commenceBankruptcyProceedings(): RollForMoveResult {
        return RollForMoveResult(
            diceRoll = diceRoll,
            destinationSquare = Square.NULL,
            outcome = RollForMoveOutcome.REMAIN_IN_JAIL
        )
    }

    private fun remainInJail(): RollForMoveResult {
        return RollForMoveResult(
            diceRoll = diceRoll,
            destinationSquare = Square.NULL,
            outcome = RollForMoveOutcome.REMAIN_IN_JAIL
        )
    }

}