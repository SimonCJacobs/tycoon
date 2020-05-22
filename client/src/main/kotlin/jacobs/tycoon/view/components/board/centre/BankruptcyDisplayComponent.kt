package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.domain.players.Player

class BankruptcyDisplayComponent (
    private val bankruptPlayer: Player,
    private val continueLambda: () -> Unit,
    override var squaresToASideExcludingCorners: Int
) : CentreCellComponent() {

    @Suppress( "unused" )
    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            children(
                m( Tag.h5 ){ content( "Sadly $bankruptPlayer has gone bankrupt" ) },
                m( Tag.button ) {
                    attributes( object {
                        val type = "button"
                    } )
                    content( "Ok. Let's press on" )
                    eventHandlers {
                        onclick = { continueLambda() }
                    }
                }
            )
        }
    }

    @Suppress( "unused" )
    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {
            val fontSize = "50px"
        }
    }

}