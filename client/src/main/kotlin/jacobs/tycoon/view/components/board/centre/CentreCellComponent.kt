package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.Tag
import jacobs.mithril.m
import org.js.mithril.Component
import org.js.mithril.VNode

abstract class CentreCellComponent : Component {

    abstract var squaresToASideExcludingCorners: Int

    @Suppress( "unused" )
    final override fun view(): VNode {
        return m( Tag.td ) {
            attributes ( object {
                val colspan = squaresToASideExcludingCorners
                val rowspan = squaresToASideExcludingCorners - 1
                val style = getStyleObject()
            } )
            getContent()()
        }
    }

    protected abstract fun getContent(): HyperScriptBuilder.Details.() -> Unit
    protected abstract fun getStyleObject(): CentreCellStylesBase

}