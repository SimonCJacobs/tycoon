package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.tycoon.domain.players.Player

class WinnerDisplayComponent (
    private val winner: Player,
    override var squaresToASideExcludingCorners: Int
) : CentreCellComponent() {


    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            content( "$winner is the winner!" )
        }
    }

    @Suppress( "unused" )
    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {
            val fontSize = "50px"
        }
    }

}