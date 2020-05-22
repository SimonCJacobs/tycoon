package jacobs.tycoon.domain.board.cards

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.rules.MiscellaneousRules
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Standard decks are only 16 cards.
 *
 * Source: https://monopolyguide.com/london/monopoly-london-list-of-community-chest-cards/
 */
@Serializable
class CommunityChestCards private constructor() : CardSet() {

    companion object {
        fun fromBoardAndRules( board: Board, gameRules: MiscellaneousRules, chanceKey: String ): CommunityChestCards {
            return CommunityChestCards()
                .apply {
                    this.chanceKey = chanceKey
                    this.cardContentsList = this.populateCardContentsList( board, gameRules )
                }
        }
    }

    private var chanceKey: String = ""

    @Transient
    override var cardContentsList: List < Pair < String, CardAction > > = emptyList()
        private set

    override val getOutOfJailFreeCardCount: Int
        get() = 1

    private fun populateCardContentsList( board: Board, gameRules: MiscellaneousRules )
            : List < Pair < String, CardAction > > {
        val currency: ( Int ) -> CurrencyAmount = { CurrencyAmount( it, board.currency ) }
        val tenPounds = currency( 10 )
        return listOf(
            this.advanceTo( "Advance to \"${ board.goSquare.name }\"", board.goSquare ),
            this.goBackTo( "Go back to ${ board.oldKentRoad.name }", board.oldKentRoad ),
            this.goToJail( board.jailSquare, board.goSquare, gameRules.goCreditAmount ),
            this.payMoneyAction( currency( 100 ), "hospital" ) { "Pay hospital $it" },
            this.payMoneyAction( currency( 50 ), "doctor" ) { "Doctor's fee. Pay $it" },
            this.payMoneyAction( currency( 50 ), "insurance" ) { "Pay your insurance premium $it" },
            this.receiveMoneyAction( currency( 200 ) )
                { "Bank error in your favour. Collect $it" },
            this.receiveMoneyAction( currency( 100 ) ) { "Annuity matures. Collect $it" },
            this.receiveMoneyAction( currency( 100 ) ) { "You inherit $it" },
            this.receiveMoneyAction( currency( 50 ) ) { "From sale of stock you get $it" },
            this.receiveMoneyAction( currency( 25 ) )
                { "Receive interest on 7% preference shares: $it" },
            this.receiveMoneyAction( currency( 20 ) )
                { "Income tax refund. Collect $it" },
            this.receiveMoneyAction( currency( 10 ) )
                { "You have won second prize in a beauty contest. Collect $it" },
            "It is your birthday. Collect $tenPounds from each player" to
                    { player -> paymentsDueFromAll( player, player, tenPounds, "birthday" ) },
            "Pay a $tenPounds fine or take a \"Chance\"" to
                { player -> payFineOrTakeCard( player, tenPounds, getChanceCards( board ) ) }
        )
    }

    private fun getChanceCards( board: Board ): CardSet {
        return board.getNamedCardSet( chanceKey )
    }

}