package jacobs.tycoon.domain.board.cards

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class CommunityChestCards : CardSet() {

    @Transient
    override val cardList: List < Card > = listOf(
        Card( "Advance to \"Go\"" )
            { game, player -> movingAPiece( player, game.board.goSquare ) }
    )

}