package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.AdminController
import jacobs.tycoon.clientstate.AdminState
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.HTMLInputElement

class AdminPage ( kodein: Kodein ) : Page {

    private val adminState by kodein.instance < AdminState > ()
    private val controller by kodein.instance < AdminController > ()
    private val gameState by kodein.instance < GameState > ()

    override fun view(): VNode {
        return m( Tag.div ) {
            children( getPlayerModificationBoxes() )
        }
    }

    private fun getPlayerModificationBoxes(): Collection < VNode > {
        return this.gameState.game().players.activeList().map { this.getPlayerModificationBox( it ) }
    }

    @Suppress( "unused", "UNUSED_VARIABLE")
    private fun getPlayerModificationBox( player: Player ): VNode {
        this.controller.recordNewPlayerIfNecessary( player )
        val properties = this.controller.getPlayerProperties( player )
        return m( Tag.div ) {
            attributes( object {
                val style = object {
                    val display = "flex"
                }
            } )
            children(
                m( Tag.h4 ) { content( player.name ) },
                m( Tag.h5 ) { content( player.cashHoldings.toString() ) },
                m( Tag.input ) {
                    attributes ( object {
                        val type = "text"
                        val value = properties.newCashHoldings.toString()
                    } )
                    eventHandlers {
                        onInputExt = {
                            ( it.target as HTMLInputElement).value.toIntOrNull()?.let {
                                theResultingInteger -> properties.newCashHoldings = theResultingInteger
                            }
                        }
                    }
                },
                m( Tag.button ) {
                    eventHandlers { onclick = { controller.updateCashHoldings( player ) } }
                }
            )
        }
    }

}