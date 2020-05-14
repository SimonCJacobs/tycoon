package jacobs.tycoon.domain.actions.gameadmin

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.board.Board
import kotlinx.serialization.Serializable

@Serializable
class SetBoard (
    val board: Board,
    var shuffleOrders: List < List < Int > > = listOf( emptyList() )
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    /**
     * The idea between the card shuffling is to ensure the client and the server
     * both know the order of the decks so their states of the game remain in sync.
     *
     * This is done by shuffling the cards server-side in [execute], passing the
     * result of those shuffles back to the client via [shuffleOrders], then
     * having the client apply the same orders in [duplicate].
     */
    override suspend fun duplicate( gameController: GameController) {
        gameController.setGameBoard( board, shuffleOrders )
    }

    override suspend fun execute( gameController: GameController ) {
        this.shuffleOrders = this.board.shuffleCards()
        gameController.setGameBoard( board )
            .also { setExecutionResult( it ) }
    }

}