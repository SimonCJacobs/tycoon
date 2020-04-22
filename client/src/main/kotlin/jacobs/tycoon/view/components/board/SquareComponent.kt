package jacobs.tycoon.view.components.board

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.jsutilities.jsObject
import jacobs.mithril.m
import jacobs.mithril.Tag

abstract class SquareComponent(
    private val name : String
) : Component {

    final override fun view(): VNode {
        return m( Tag.td ) {
            attributes {
                style = jsObject {
                    border = "1.2px solid black"
                    textAlign = "center"
                }
            }
            content( name )
        }
    }

}