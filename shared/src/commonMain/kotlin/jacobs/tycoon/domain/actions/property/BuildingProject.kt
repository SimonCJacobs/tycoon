package jacobs.tycoon.domain.actions.property

import jacobs.tycoon.domain.bank.Bank
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.players.Player

class BuildingProject(
    private val streets: List < Street >,
    private val newHouses: List < Int >, // Ignoring hotels here
    private val player: Player,
    private val bank: Bank
) {
        // Ignoring hotels
    private val newDistribution = newHouses.mapIndexed {
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
        player.debitFunds( streets[ 0 ].colourGroup.costOfHouse() * newHouses.sum() )
        streets.forEachIndexed { index, street -> street.buildHouses( newHouses[ index ] ) }
        bank.housesInStock = bank.housesInStock - totalHousesChange()
        bank.hotelsInStock = bank.hotelsInStock - totalHotelsChange()
    }

    fun isValid(): Boolean {
        return this.streets.size == newHouses.size &&
            this.streets.isNotEmpty() &&
            this.areAllStreetsOfSameColourGroup() &&
            this.isDevelopmentEven() &&
            this.doesActorOwnProperties() &&
            this.isDevelopmentWithinMaximumBuildingPerProperty() &&
            this.canActorAffordTheDevelopment() &&
            this.doesTheBankHaveSufficientHousingStock()
    }

    private fun areAllStreetsOfSameColourGroup(): Boolean {
        return streets.all { it.colourGroup == streets[ 0 ].colourGroup }
    }

    private fun isDevelopmentEven(): Boolean {
        return newDistribution.max()!! - newDistribution.min()!! <= 1
    }

    private fun doesActorOwnProperties(): Boolean {
        return this.streets.all { player.owns( it ) }
    }

    private fun isDevelopmentWithinMaximumBuildingPerProperty(): Boolean {
        return this.newDistribution.all { it <= bank.housesToAHotel }
    }

    private fun canActorAffordTheDevelopment(): Boolean {
        val singleHouseCost = this.streets[ 0 ].colourGroup.costOfHouse()
        val totalCost = singleHouseCost * this.newHouses.sum()
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