package jacobs.tycoon.view.components.console

import jacobs.jsutilities.jsObject
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

    override fun view(): VNode {
        return m( Tag.aside ) {
            attributes {
                style = jsObject {
                    border = "1px solid black"
                }
            }
            child( getLogList() )
        }
    }

    private fun getLogList(): VNode {
        return m( Tag.ol ) {
            attributes {
                reversed = true
            }
            children( getLogsReversed() )
        }
    }

    private fun getLogsReversed(): List < VNode > {
        return this.getTextLogs().map { getLogFromText( it ) }
            .reversed()
    }

    private fun getTextLogs(): List < String > {
        return this.gameHistory.processAllLogs( this.processor )
    }

    private fun getLogFromText( text: String ): VNode {
        return m( Tag.li ) { content( text )}
    }

}