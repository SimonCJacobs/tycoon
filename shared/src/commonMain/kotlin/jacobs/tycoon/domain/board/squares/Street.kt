package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.colourgroups.ColourGroup
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Street (
    override val indexOnBoard: Int,
    override val name: String,
    override val listPrice: CurrencyAmount,
    val rentCard: RentCard,
    @Transient val colourGroup: ColourGroup = ColourGroup.NULL
) : OwnershipProperty() {

    var numberOfHousesBuilt: Int = 0 // Here hotels are viewed as 5 houses

    override fun < T > accept( propertyVisitor: PropertyVisitor < T > ): T {
        return propertyVisitor.visit( this )
    }

    override fun < T > accept( squareVisitor: SquareVisitor < T >): T {
        return squareVisitor.visit( this )
    }

    fun buildHouses( newHouseCount: Int ) {
        this.numberOfHousesBuilt = this.numberOfHousesBuilt + newHouseCount
    }

    override fun calculateRent( owner: Player ): CurrencyAmount {
        return when {
            owner.howManyOfColourGroupOwned( colourGroup ) < colourGroup.numberInGroup
                -> rentCard.rentByHouseCount( 0 )
            numberOfHousesBuilt == 0 -> rentCard.rentByHouseCount( 0 ) * 2
            numberOfHousesBuilt > rentCard.maximumHouseCount() ->
                throw Error( "Too many houses. $numberOfHousesBuilt when maximum " +
                    rentCard.maximumHouseCount() )
            else -> rentCard.rentByHouseCount( numberOfHousesBuilt )
        }
    }

    fun sellHouses( soldHouseCount: Int ) {
        this.numberOfHousesBuilt = this.numberOfHousesBuilt - soldHouseCount
    }

}