package jacobs.tycoon.view.components.console

import jacobs.mithril.m
import jacobs.mithril.Tag
import jacobs.tycoon.services.ActionWriter
import jacobs.tycoon.state.GameHistory
import org.js.mithril.Component
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Console( kodein: Kodein ) : Component {

    private val gameHistory by kodein.instance < GameHistory > ()
    private val processor by kodein.instance < ActionWriter > ()

        // A list from the first log to the last. So needs to be reversed prior to display
    private val gameLogs: MutableList < String > = mutableListOf()

    @Suppress( "unused" )
    override fun view(): VNode {
        return m( Tag.aside ) {
            attributes ( object {
                val style = object {
                    val border = "1px solid black"
                }
            } )
            child( getLogList() )
        }
    }

    @Suppress( "unused" )
    private fun getLogList(): VNode {
        this.updateGameLogs()
        return m( Tag.ol ) {
            attributes ( object {
                val reversed = true
            } )
            children( getLogNodesReversed() )
        }
    }

    private fun getLogNodesReversed(): List < VNode > {
        return this.gameLogs.reversed()
            .map { getLogFromText( it ) }
    }

    private fun getLogFromText( text: String ): VNode {
        return m( Tag.li ) { content( text )}
    }

    private fun updateGameLogs() {
        this.gameLogs.addAll(
            this.gameHistory.processHistoryFromIndex( this.processor, this.gameLogs.size )
        )
    }

}