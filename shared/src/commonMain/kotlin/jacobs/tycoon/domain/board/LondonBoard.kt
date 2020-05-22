package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.cards.CardSet
import jacobs.tycoon.domain.board.cards.ChanceCards
import jacobs.tycoon.domain.board.cards.CommunityChestCards
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.rules.MiscellaneousRules
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class LondonBoard(
    override val currency: Currency
) : StandardBoard() {

    @Transient override val location: String = "London"
    @Transient override var cardSets: Map < String, CardSet > = emptyMap()

        // Order is important to get the card sets lining up to the right squares
    override fun initialiseCardSets( gameRules: MiscellaneousRules ) {
        this.cardSets = mapOf(
            COMMUNITY_CHEST_CARDS_NAME to
                CommunityChestCards.fromBoardAndRules( this, gameRules, CHANCE_CARDS_NAME ),
            CHANCE_CARDS_NAME to
                ChanceCards.fromBoardAndRules( this, gameRules )
        )
    }

    override fun nameListProvider(): List < String > {
        return listOf(
            "Go",
            "Old Kent Road", "Community Chest", "Whitechapel Road", "Income Tax",
            "King's Cross Station",
            "The Angel, Islington", "Chance", "Euston Road", "Pentonville Road",
            "Jail",
            "Pall Mall", "Electric Company", "Whitehall", "Northumberland Avenue",
            "Marylebone Station",
            "Bow Street", "Community Chest", "Marlborough Street", "Vine Street",
            "Free Parking",
            "Strand", "Chance", "Fleet Street", "Trafalgar Square",
            "Fenchurch Street Station",
            "Leicester Square", "Coventry Street", "Water Works", "Piccadilly",
            "Go To Jail",
            "Regent Street", "Oxford Street", "Community Chest", "Bond Street",
            "Liverpool Street Station",
            "Chance", "Park Lane", "Super Tax", "Mayfair"
        )
    }

}