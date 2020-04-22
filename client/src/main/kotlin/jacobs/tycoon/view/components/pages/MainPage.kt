package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import org.js.mithril.VNode
import jacobs.mithril.m
import jacobs.tycoon.controller.MainController
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.console.Console
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainPage ( kodein: Kodein ) : Page {

    private val state: GameStateProvider by kodein.instance()
    private val controller: MainController by kodein.instance()
    private val board: BoardComponent by kodein.instance()
    private val gameConsole: Console by kodein.instance()

    override fun view() : VNode {
        return m( Tag.div ) {
            children( m( board ), m( gameConsole ) )
        }
    }

}