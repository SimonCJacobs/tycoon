package jacobs.tycoon.view.components.board.centre

import jacobs.tycoon.clientcontroller.DealController
import jacobs.tycoon.domain.board.squares.Street

class BuildingForm ( dealController: DealController ) : FormByStreet() {

    override val title: String = "Houses to build"
    override val streetListProvider: () -> List < Street > =
        { dealController.streetsSelectedForHouseBuilding() }
    override val streetStateProvider: ( Street ) -> Int =
        { dealController.housesToBuildOnStreet( it ) }
    override val streetStateUpdate: ( Street, Double ) -> Unit =
        { street, count -> dealController.updateHousesToBuildOnStreet( street, count ) }
    override val streetMaxProvider: ( Street ) -> Int =
        { dealController.maxHousesCanBuildOnStreet( it ) }

}