package jacobs.tycoon.view.components.console

import jacobs.tycoon.domain.logs.ActionLog
import jacobs.tycoon.domain.logs.LogProcessor
import jacobs.jsutilities.jsObject
import jacobs.mithril.m
import jacobs.mithril.Tag
import org.js.mithril.Component
import org.js.mithril.VNode

class Console( private val log: ActionLog, private val processor: LogProcessor < String > ) : Component {

    override fun view(): VNode {
        return m( Tag.aside ) {
            attributes {
                style = jsObject {
                    border = "1px solid black"
                }
            }
            if ( log.isEmpty() )
                content( "Waiting to start..." )
            else
                child( getLogListAsOne() )
        }
    }

    private fun getLogListAsOne() : VNode {
        return m( Tag.ol ) {
            children( getLogsListed() )
        }
    }

    private fun getLogsListed(): List < VNode > {
        return this.log.process( this.processor )
            .map{
                m( Tag.li ) { content( it ) }
            }
    }

}