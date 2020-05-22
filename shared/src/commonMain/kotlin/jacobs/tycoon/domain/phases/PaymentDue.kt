package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.Player

open class PaymentDue (
    override val playerWithTurn: Player,
    private val playersOwingMoney: List < Player >,
    val amountDue: CurrencyAmount,
    val reason: String,
    val playerOwedMoney: Player?
) : TurnBasedPhase {

    constructor(
        playerWithTurn: Player,
        playerOwingMoney: Player,
        amountDue: CurrencyAmount,
        reason: String,
        playerOwedMoney: Player?
    ) : this ( playerWithTurn, listOf( playerOwingMoney ), amountDue, reason, playerOwedMoney )

    val bankruptciesPending: MutableList < Player > = mutableListOf()
    private val playersThatHaveAttemptedPayment: MutableList < Player > = mutableListOf()

    fun attemptPayment( payingPlayer: Player ) {
        playersThatHaveAttemptedPayment.add( payingPlayer )
        if ( payingPlayer.cashHoldings >= amountDue ) {
            payingPlayer.debitFunds( amountDue )
            playerOwedMoney?.apply { creditFunds( amountDue ) }
        }
        else
            bankruptciesPending.add( payingPlayer )
    }

    fun doesPlayerStillOweMoney( player: Player ): Boolean {
        return playersOwingMoney.contains( player ) && false == playersThatHaveAttemptedPayment.contains( player )
    }

    fun hasPlayerAttemptedPayment( player: Player ): Boolean {
        return this.playersThatHaveAttemptedPayment.contains( player )
    }

    fun haveAllPaymentsBeenMade(): Boolean {
        return playersThatHaveAttemptedPayment.size == playersOwingMoney.size
    }

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}