package jacobs.tycoon.testdata.gamestate

import jacobs.tycoon.domain.board.cards.ShuffleOrders
import org.kodein.di.Kodein

class GameStateBuilder {

    var overrides: Kodein.MainBuilder.() -> Unit = {}
    var shuffleOrders: ShuffleOrders? = null

}