package jacobs.tycoon.domain.actions.property

import jacobs.tycoon.domain.bank.Bank
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.players.Player

//TODO: Auction for final houses!!
class BuildingProject(
    private val streets: List < Street >,
    private val houseChangeDistribution: List < Int >, // Ignoring hotels here
    private val player: Player,
    private val bank: Bank
) {
        // Ignoring hotels
    private val newDistribution = houseChangeDistribution.mapIndexed {
        index, houseCount -> houseCount + streets[ index ].numberOfHousesBuilt
    }
        // Now factoring in hotels
    private val newBuildings = streets.mapIndexed {
        index, street -> getNewBuildings( newDistribution[ index ], street )
    }

    private fun getNewBuildings( newHouseCount: Int, street: Street ): BuildingsChange {
        return when ( newHouseCount ) {
            street.numberOfHousesBuilt -> BuildingsChange( 0, 0 )
            bank.housesToAHotel -> BuildingsChange( -1 * street.numberOfHousesBuilt, 1 )
            else -> BuildingsChange( newHouseCount - street.numberOfHousesBuilt, 0 )
        }
    }

    fun carryOut() {
        player.debitFunds( streets[ 0 ].colourGroup.costOfHouse() * houseChangeDistribution.sum() )
        streets.forEachIndexed { index, street -> street.buildHouses( houseChangeDistribution[ index ] ) }
        bank.housesInStock = bank.housesInStock - totalHousesChange()
        bank.hotelsInStock = bank.hotelsInStock - totalHotelsChange()
    }

    fun isValid(): Boolean {
        return this.streets.size == houseChangeDistribution.size &&
            this.streets.isNotEmpty() &&
            this.areAllStreetsOfSameColourGroup() &&
            this.player.isStreetOwnedInAFullColourGroup( this.streets [ 0 ] ) &&
            this.isDevelopmentEven() &&
            this.isDevelopmentWithinMaximumBuildingPerProperty() &&
            this.isDevelopmentWithinMinimumBuildingPerProperty() &&
            this.canActorAffordTheDevelopment() &&
            this.doesTheBankHaveSufficientHousingStock()
    }

    private fun areAllStreetsOfSameColourGroup(): Boolean {
        return streets.all { it.colourGroup == streets[ 0 ].colourGroup }
    }

    private fun isDevelopmentEven(): Boolean {
        return newDistribution.maxOrNull()!! - newDistribution.minOrNull()!! <= 1
    }

    private fun isDevelopmentWithinMinimumBuildingPerProperty(): Boolean {
        return this.newDistribution.all { it >= 0 }
    }

    private fun isDevelopmentWithinMaximumBuildingPerProperty(): Boolean {
        return this.newDistribution.all { it <= bank.housesToAHotel }
    }

    private fun canActorAffordTheDevelopment(): Boolean {
        val singleHouseCost = this.streets[ 0 ].colourGroup.costOfHouse()
        val totalCost = singleHouseCost * this.houseChangeDistribution.sum()
        return player.cashHoldings >= totalCost
    }

    private fun doesTheBankHaveSufficientHousingStock(): Boolean {
        return totalHousesChange() <= bank.housesInStock && totalHotelsChange() <= bank.hotelsInStock
    }

    private fun totalHousesChange(): Int {
        return newBuildings.map { it.houses }.sum()
    }

    private fun totalHotelsChange(): Int {
        return newBuildings.map { it.hotels }.sum()
    }

    data class BuildingsChange( val houses: Int, val hotels: Int )

}