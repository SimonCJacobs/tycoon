package jacobs.tycoon.view.components

import external.mithril.Component
import external.mithril.VNode
import jacobs.jsutilities.jsObject
import jacobs.mithril.HyperScriptFactory
import jacobs.mithril.Tag

abstract class SquareComponent(
    private val m: HyperScriptFactory,
    private val name : String
) : Component {

    final override fun view(): VNode {
        return m( Tag.td ) {
            attributes {
                style = jsObject {
                    border = "1.2px solid black"
                }
            }
            contents( name )
        }
    }

}