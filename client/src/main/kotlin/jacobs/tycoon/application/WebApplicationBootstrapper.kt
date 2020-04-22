package jacobs.tycoon.application

import jacobs.mithril.Mithril
import jacobs.tycoon.controller.MainController
import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.domain.LondonBoard
import jacobs.tycoon.network.Network
import jacobs.tycoon.view.MainPage
import jacobs.tycoon.view.View
import jacobs.tycoon.view.components.BoardComponent
import jacobs.tycoon.view.components.SquareComponentFactory
import kotlinx.coroutines.MainScope
import kotlin.browser.document

class WebApplicationBootstrapper {

    fun bootstrap() {
        this.createApplication()
            .start()
    }

    private fun createApplication(): Application {
        val game = Game()
        val scope = MainScope()
        val network = Network( scope )
        val mainController = MainController( game, network )
        val gameStateProvider = GameStateProvider( game )
        val mainElement = document.getElementById( "main" )!!
        val board = LondonBoard()
        val squareComponentFactory = SquareComponentFactory()
        val boardComponent = BoardComponent( board, squareComponentFactory )
        val mainPage = MainPage(
            gameStateProvider, mainController, boardComponent
        )
        val view = View( mainElement, mainPage )
        return Application( scope, network, view )
    }

}