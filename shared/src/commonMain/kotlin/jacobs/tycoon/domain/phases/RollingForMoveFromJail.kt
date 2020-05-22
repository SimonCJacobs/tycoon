package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.phases.results.FailedEscapeResult
import jacobs.tycoon.domain.phases.results.JailOutcome
import jacobs.tycoon.domain.phases.results.MovingOutOfJailWithDiceResult
import jacobs.tycoon.domain.phases.results.NonDiceJailResult
import jacobs.tycoon.domain.phases.results.RollForMoveFromJailResult
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.JailRules

class RollingForMoveFromJail(
    playerWithTurn: Player,
    private val jailRules: JailRules
) : RollingForMoveBase( playerWithTurn ) {

    val jailFine: CurrencyAmount
        get() = jailRules.leaveJailFineAmount
    lateinit var result: RollForMoveFromJailResult

    fun decideResultOfDiceRoll( game: Game ): RollForMoveFromJailResult {
        this.playerWithTurn.addDiceRoll( diceRoll )
        this.result = when {
            diceRoll.isDouble() -> this.doDiceMoveWithoutFine( game )
            playerWithTurn.howManyRollsHadOnCurrentSquare() == jailRules.mustLeaveRollCount
                -> this.payFineAndLeaveJail( game )
            else -> FailedEscapeResult( JailOutcome.REMAIN_IN_JAIL, diceRoll )
        }
        return this.result
    }

    fun payFine(): RollForMoveFromJailResult {
        if ( playerWithTurn.cashHoldings < jailRules.leaveJailFineAmount )
            throw Error( "Should not be permitted to pay fine if not have funds to do so" )
        playerWithTurn.debitFunds( jailRules.leaveJailFineAmount )
        return NonDiceJailResult( JailOutcome.PAID_FINE_VOLUNTARILY )
            .also { this.result = it }
    }

    fun useGetOutOfJailFreeCard(): RollForMoveFromJailResult {
        playerWithTurn.disposeOfGetOutOfJailFreeCards( 1 )
            .single()
            .returnToDeck()
        return NonDiceJailResult( JailOutcome.USED_CARD )
            .also { this.result = it }
    }

    private fun doDiceMoveWithoutFine( game: Game ): RollForMoveFromJailResult {
        return MovingOutOfJailWithDiceResult(
            this.waitForPlayerToMove( game ),
            JailOutcome.ROLLED_A_DOUBLE
        )
    }

    private fun payFineAndLeaveJail( game: Game ): RollForMoveFromJailResult {
        return MovingOutOfJailWithDiceResult(
            this.waitForPlayerToMove( game ),
            JailOutcome.FORCED_TO_PAY_FINE
        )
    }

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}