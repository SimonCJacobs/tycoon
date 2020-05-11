package jacobs.tycoon.view.components.board

import jacobs.mithril.Tag
import jacobs.mithril.m
import org.js.mithril.Component
import org.js.mithril.VNode

abstract class CentreCellComponent(
    private val squaresToASideExcludingCorners: Int
) : Component {

    @Suppress( "unused" )
    final override fun view(): VNode {
        return m( Tag.td ) {
            attributes ( object {
                val colspan = squaresToASideExcludingCorners
                val rowspan = squaresToASideExcludingCorners - 1
                val style = object {
                    val fontSize = getFontSize()
                    val fontStyle = getFontStyle()
                    val position = "relative"       // Position below the game squares
                    val textAlign = "center"
                    val transform = getTransform()
                    val zIndex = -1                 // Position below the game squares
                }
            } )
            content( getText() )
        }
    }

    protected abstract fun getFontSize(): String?
    protected abstract fun getFontStyle(): String?
    protected abstract fun getText(): String
    protected abstract fun getTransform(): String?

}