package jacobs.tycoon.domain.board.cards

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ChanceCards : CardSet() {

    @Transient
    override val cardContentsList: List < Pair < String, CardAction > > = listOf(
        "Advance to \"Go\"" to
            { game, player -> movingAPiece( player, game.board.goSquare ) }
    )

}