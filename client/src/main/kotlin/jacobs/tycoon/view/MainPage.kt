package jacobs.tycoon.view

import jacobs.mithril.Tag
import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.mithril.m
import jacobs.tycoon.controller.MainController
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.console.Console

class MainPage (
    private val state: GameStateProvider,
    private val controller: MainController,
    private val board: BoardComponent,
    private val gameConsole: Console
) : Component {

    override fun view() : VNode {
        return m( Tag.div ) {
            children( m( board ), m( gameConsole ) )
        }
    }

}