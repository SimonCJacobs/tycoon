package jacobs.tycoon.view

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.mithril.m
import jacobs.tycoon.controller.MainController
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.view.components.BoardComponent

class MainPage (
    private val state: GameStateProvider,
    private val controller: MainController,
    private val board: BoardComponent
) : Component {

    override fun view() : VNode {
        return m( this.board )
    }

}