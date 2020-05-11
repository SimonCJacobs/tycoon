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
    @Transient val colourGroup: ColourGroup = ColourGroup.NULL
) : OwnershipProperty() {

    override fun < T > accept( propertyVisitor: PropertyVisitor < T > ): T {
        return propertyVisitor.visit( this )
    }

    override fun < T > accept( squareVisitor: SquareVisitor < T >): T {
        return squareVisitor.visit( this )
    }

    override fun calculateRent( owner: Player): CurrencyAmount {
        return listPrice.half()
    }

}