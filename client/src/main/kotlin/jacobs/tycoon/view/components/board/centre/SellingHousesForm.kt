package jacobs.tycoon.view.components.board.centre

import jacobs.tycoon.clientcontroller.DealController
import jacobs.tycoon.domain.board.squares.Street

class SellingHousesForm (dealController: DealController ) : FormByStreet() {

    override val title: String = "Houses to sell"
    override val streetListProvider: () -> List < Street > =
        { dealController.streetsSelectedForHouseSelling() }
    override val streetStateProvider: ( Street ) -> Int =
        { dealController.housesToSellOnStreet( it ) }
    override val streetStateUpdate: ( Street, Double ) -> Unit =
        { street, count -> dealController.updateHousesToSellOnStreet( street, count ) }
    override val streetMaxProvider: ( Street ) -> Int =
        { dealController.maxHousesCanSellOnStreet( it ) }

}