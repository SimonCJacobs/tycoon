package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder

class LogoCentreCellComponent (
    private val gameName: String,
    override var squaresToASideExcludingCorners: Int
) : CentreCellComponent() {


    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return { content( gameName ) }
    }

    @Suppress( "unused" )
    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {
            val fontSize = "80px"
            val fontStyle = "italic"
            val position = "relative"
            val transform = "rotate( -0.048turn )"
            val zIndex = -1
        }
    }

}