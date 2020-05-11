package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class SetBoard (
    val board: Board,
    var shuffleOrders: List < List < Int > > = listOf( emptyList() )
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
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
        this.board.initialise()
        this.board.applyShuffleOrders( shuffleOrders )
        this.setGameBoard( gameController, this.board )
    }

    override suspend fun execute( gameController: GameController ) {
        this.board.initialise()
        this.shuffleOrders = this.board.shuffleCards()
        this.setGameBoard( gameController, this.board )
        this.executedSuccessfully()
    }

    private fun setGameBoard(  gameController: GameController, board: Board ) {
        gameController.game().board = board
    }

}