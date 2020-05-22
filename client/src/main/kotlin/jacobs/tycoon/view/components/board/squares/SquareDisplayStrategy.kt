package jacobs.tycoon.view.components.board.squares

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Street
import org.js.mithril.VNode

sealed class SquareDisplayStrategy {
    open fun extraNode(): VNode? = null
    abstract fun getClass(): String
    open fun handleClick() {}
}

object NormalPlayDisplay : SquareDisplayStrategy() {
    override fun getClass(): String {
        return "square"
    }
}

object DestinationSquareDisplay : SquareDisplayStrategy() {
    override fun getClass(): String {
        return "square drop-target"
    }
}

abstract class SelectableDisplay : SquareDisplayStrategy() {
    protected abstract val property: Property
    protected abstract val dealController: DealController

    override fun extraNode(): VNode? {
        val localProperty = property
        return when {
            false == localProperty is Street -> null
            localProperty.hasAnyDevelopment() == false -> null
            localProperty.hasHotel() -> developmentNode( showHotel() )
            else -> developmentNode( showHouses( localProperty.numberOfHousesBuilt ))
        }
    }

    override fun handleClick() {
        dealController.handleClickOnProperty( property )
    }

    private fun developmentNode( textContent: String ): VNode {
        return m( Tag.h6 ) {
            content( textContent )
        }
    }

    private fun showHouses( count: Int ): String {
        return "🏠".repeat( count )
    }

    private fun showHotel(): String {
        return "🏨"
    }
}

class PrimarySelectableDisplay(
    override val dealController: DealController,
    override val property: Property
)  : SelectableDisplay() {

    override fun getClass(): String {
        return "square available-for-selection" +
            if ( dealController.isSelected( property ) )
                " selected"
            else
                ""
    }
}

class SelectableSecondaryDisplay(
    override val dealController: DealController,
    override val property: Property
)  : SelectableDisplay() {

    override fun getClass(): String {
        return "square available-for-selection-secondary" +
            if ( dealController.isSelected( property ) )
                " selected-secondary"
            else
                ""
    }
}

object UnavailableDisplay : SquareDisplayStrategy() {
    override fun getClass(): String {
        return "square unavailable-for-selection"
    }
}