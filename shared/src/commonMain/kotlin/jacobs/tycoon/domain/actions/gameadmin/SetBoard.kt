package jacobs.tycoon.domain.actions.gameadmin

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.cards.ShuffleOrders
import kotlinx.serialization.Serializable

@Serializable
class SetBoard(
    val board: Board,
    var shuffleOrders: ShuffleOrders? = null
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    /**
     * The idea between the card shuffling is to ensure the client and the server
     * both know the order of the decks so their states of the game remain in sync.
     *
     * This is done by shuffling the cards server-side when [shuffleOrders] is null, passing the
     * result of those shuffles back to the client via [shuffleOrders], then
     * having the client apply the same orders in [duplicate] when [shuffleOrders] is not null.
     */
    override suspend fun duplicate( gameController: GameController) {
        execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        if ( null == this.shuffleOrders )
            this.shuffleOrders = gameController.setGameBoard( board )
        else
            gameController.setGameBoard( board, this.shuffleOrders )
        executedSuccessfully()
    }

}