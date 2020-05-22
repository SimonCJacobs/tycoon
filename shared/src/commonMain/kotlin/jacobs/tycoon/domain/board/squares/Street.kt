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
    @Transient val colourGroup: ColourGroup = ColourGroup.NULL,
    private val housesToAHotel: Int
) : OwnershipProperty() {

    companion object {
        val NULL = Street( -1, "", CurrencyAmount.NULL, RentCard.NULL, ColourGroup.NULL, 0 )
    }

    var numberOfHousesBuilt: Int = 0 // At this stage, hotels are considered to be more houses.

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
            owner.isStreetOwnedInAFullColourGroup( this ) == false -> rentCard.rentByHouseCount( 0 )
            numberOfHousesBuilt == 0 -> rentCard.rentByHouseCount( 0 ) * 2
            numberOfHousesBuilt > rentCard.maximumHouseCount() ->
                throw Error( "Too many houses. $numberOfHousesBuilt when maximum " +
                    rentCard.maximumHouseCount() )
            else -> rentCard.rentByHouseCount( numberOfHousesBuilt )
        }
    }

    fun getNumberOfHousesExcludingHotels(): Int {
        if ( hasHotel() )
            return 0
        else
            return numberOfHousesBuilt
    }

    fun hasAnyDevelopment(): Boolean {
        return this.numberOfHousesBuilt > 0
    }

    fun hasHotel(): Boolean {
        return this.housesToAHotel == numberOfHousesBuilt
    }

    fun hasOwnedDevelopment( expectedOwner: Player ): Boolean {
        return expectedOwner.owns( this ) && this.hasAnyDevelopment()
    }

    fun hasScopeForDevelopmentBy( player: Player ): Boolean {
        return player.owns( this ) &&
            player.isStreetOwnedInAFullColourGroup( this ) &&
            this.hasHotel() == false
    }

    fun sellHouses( soldHouseCount: Int ) {
        this.numberOfHousesBuilt = this.numberOfHousesBuilt - soldHouseCount
    }

}