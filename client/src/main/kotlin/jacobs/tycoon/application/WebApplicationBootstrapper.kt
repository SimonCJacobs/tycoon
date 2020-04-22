package jacobs.tycoon.application

import jacobs.tycoon.controller.MainController
import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.logs.ActionLog
import jacobs.tycoon.network.Network
import jacobs.tycoon.view.MainPage
import jacobs.tycoon.view.View
import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.board.SquareComponentFactory
import jacobs.tycoon.view.components.console.Console
import jacobs.tycoon.view.components.console.LogWriter
import kotlinx.coroutines.MainScope
import kotlin.browser.document

class WebApplicationBootstrapper {

    fun bootstrap() {
        this.createApplication()
            .start()
    }

    private fun createApplication(): Application {
        val log = ActionLog()
        val game = Game( log )
        val scope = MainScope()
        val network = Network( scope )
        val mainController = MainController( game, network )
        val gameStateProvider = GameStateProvider( game )
        val mainElement = document.getElementById( "main" )!!
        val board = LondonBoard()
        val squareComponentFactory = SquareComponentFactory()
        val boardComponent = BoardComponent( board, squareComponentFactory )
        val logWriter = LogWriter()
        val gameConsole = Console( log, logWriter )
        val mainPage = MainPage(
            gameStateProvider, mainController, boardComponent, gameConsole
        )
        val view = View( mainElement, mainPage )
        return Application( scope, network, view )
    }

}