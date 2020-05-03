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

    private val existingLogs: MutableList < String > = mutableListOf()

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
            children( getExistingLogs(), getAnyNewLogs() )
        }
    }

    private fun getExistingLogs(): List < VNode > {
        return this.existingLogs.map { getLogFromText( it ) }
    }

    private fun getAnyNewLogs(): List < VNode > {
            // Cautious about accessing due to possible multi-threading issues
        val totalUpdateCount = this.gameHistory.getUpdateCount()
        if ( this.existingLogs.size == totalUpdateCount )
            return emptyList()
        else
            return this.getNewLogsUpToIndex( totalUpdateCount )
    }

    private fun getNewLogsUpToIndex( totalUpdateCount: Int ): List < VNode > {
        val newLogs = this.gameHistory.processLogsBetween(
            this.processor, this.existingLogs.size, totalUpdateCount
        )
        this.existingLogs.addAll( newLogs )
        return newLogs.map { getLogFromText( it ) }
    }

    private fun getLogFromText( text: String ): VNode {
        return m( Tag.li ) { content( text )}
    }

}