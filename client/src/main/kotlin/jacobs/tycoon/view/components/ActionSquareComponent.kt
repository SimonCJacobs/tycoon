package jacobs.tycoon.view.components

import external.mithril.VNode
import jacobs.mithril.HyperScriptFactory
import jacobs.mithril.Tag
import jacobs.tycoon.domain.ActionSquare
import jacobs.tycoon.domain.Station
import jacobs.tycoon.domain.Street

class ActionSquareComponent( private val m: HyperScriptFactory, private val actionSquare: ActionSquare )
    : SquareComponent( m, actionSquare.name ) {

}