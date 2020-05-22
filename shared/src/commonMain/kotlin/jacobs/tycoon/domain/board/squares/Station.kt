package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
class Station (
    override val indexOnBoard: Int,
    override val name: String,
    override val listPrice: CurrencyAmount,
    private val singleStationRent: CurrencyAmount
) : OwnershipProperty() {

    companion object {
        val NULL = Station( -1, "", CurrencyAmount.NULL, CurrencyAmount.NULL )
    }

    override fun < T > accept( propertyVisitor: PropertyVisitor < T > ): T {
        return propertyVisitor.visit( this )
    }

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

    override fun calculateRent( owner: Player ): CurrencyAmount {
        return when ( val stationCount = owner.howManyStationsOwned() ) {
            1 -> singleStationRent
            2 -> singleStationRent * 2
            3 -> singleStationRent * 4
            4 -> singleStationRent * 8
            else -> throw Error( "Should not get here: player owns $stationCount stations" )
        }
    }

}