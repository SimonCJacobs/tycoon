package jacobs.tycoon.view

import external.mithril.Component
import external.mithril.VNode
import jacobs.mithril.HyperScriptFactory
import jacobs.mithril.Tag
import jacobs.tycoon.controller.MainController
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.view.components.BoardComponent

class MainPage (
    private val state: GameStateProvider,
    private val controller: MainController,
    private val m: HyperScriptFactory,
    private val board: BoardComponent
) : Component {

    override fun view() : VNode {
        return m( this.board )
    }

}