package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.squares.Square
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class LondonBoard(
    override val currency: Currency
) : StandardBoard() {

    @Transient
    override val location: String = "London"

    @Transient
    override val squareList: List < Square > = this.buildSquareList( this.nameListProvider() )

    private fun nameListProvider(): List < String > {
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