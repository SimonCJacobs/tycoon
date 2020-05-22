package jacobs.tycoon.domain.board.cards

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.rules.MiscellaneousRules
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Standard decks are only 16 cards.
 *
 * Source: https://monopolyguide.com/london/monopoly-london-list-of-chance-cards/
 */
@Serializable
class ChanceCards private constructor() : CardSet() {

    companion object {
        fun fromBoardAndRules( board: Board, rules: MiscellaneousRules ): ChanceCards {
            return ChanceCards()
                .apply {
                    this.cardContentsList = this.populateCardContentsList( board, rules )
                }
        }
    }

    @Transient
    override var cardContentsList: List < Pair < String, CardAction > > = emptyList()
        private set

    override val getOutOfJailFreeCardCount: Int
        get() = 1

    private fun populateCardContentsList( board: Board, gameRules: MiscellaneousRules )
            : List < Pair < String, CardAction > > {
        val currency: ( Int ) -> CurrencyAmount = { CurrencyAmount( it, board.currency ) }
        return listOf(
            this.advanceTo( "Advance to \"${ board.goSquare.name }\"", board.goSquare ),
            this.goToJail( board.jailSquare, board.goSquare, gameRules.goCreditAmount ),
            this.advanceTo(
                "Advance to ${ board.pallMall.name }. If you pass \"${board.goSquare.name}\" collection " +
                    gameRules.goCreditAmount,
                board.pallMall
            ),
            this.advanceTo(
                "Take a trip to ${ board.maryleboneStation.name } and if you pass \"${board.goSquare.name}\" " +
                    "collect ${ gameRules.goCreditAmount }",
                board.maryleboneStation
            ),
            this.advanceTo(
                "Advance to ${ board.trafalgarSquare.name }. If you pass \"${board.goSquare.name}\" collect " +
                    gameRules.goCreditAmount,
                board.trafalgarSquare
            ),
            this.advanceTo( "Advance to ${ board.mayfair.name }", board.mayfair ),
            "Go back three spaces" to
                { player -> movingAPiece( player, board.squareMinusSpaces( player.location(), 3 ) ) },
            this.streetRepairs( currency( 25 ), currency( 100 ) )
                { "Make general repairs on all of your houses. For each house pay ${ it.first }. " +
                    "For each hotel pay ${ it.second }" },
            this.streetRepairs( currency( 40 ), currency( 115 ) )
                { "You are assessed for street repairs: ${ it.first } per house, ${ it.second } per hotel" },
            this.payMoneyAction( currency( 150 ), "school" ) { "Pay school fees of $it" },
            this.payMoneyAction( currency( 20 ), "fine" ) { "\"Drunk in charge\" fine $it" },
            this.payMoneyAction( currency( 15 ), "fine" ) { "Speeding fine $it" },
            this.receiveMoneyAction( currency( 150 ) ) { "Your building loan matures. Receive $it" },
            this.receiveMoneyAction( currency( 100 ) ) { "You have won a crossword competition. Collect $it" },
            this.receiveMoneyAction( currency( 50 ) ) { "Bank pays you dividend of $it" }  )
    }

}