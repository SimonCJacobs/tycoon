package jacobs.tycoon.view.components.players

import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.AdminController
import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.js.mithril.VNode
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document

class AdminPassiveSinglePlayerComponent(
    private val adminController: AdminController,
    override val gameState: GameState,
    override val pieceDisplayStrategy: PieceDisplayStrategy,
    override val playerActionController: PlayerActionController,
    override val player: Player
) : PassiveSinglePlayerComponent( gameState, pieceDisplayStrategy, playerActionController, player ) {

    init {
        adminController.recordNewPlayerIfNecessary( player )
    }

    override fun HyperScriptBuilder.Details.cashHoldingsNode(): VNode {
        return if ( adminController.isUpdatingCash( player ) )
                generateCashHoldingsUpdateNode()
            else
                generateCashHoldingsNode()
    }

    private fun generateCashHoldingsNode(): VNode {
        return m( Tag.div ) {
            children(
                cashHoldingsWrapper { content( getCashHoldingsString() ) },
                cashToggleUpdateModeButton()
            )
        }
    }

    private fun generateCashHoldingsUpdateNode(): VNode {
        return m( Tag.div ) {
            children(
                cashHoldingsWrapper { child( getCashUpdateInputBox() ) },
                cashUpdateButton(),
                cashToggleUpdateModeButton()
            )
        }
    }

    @Suppress( "unused" )
    private fun getCashUpdateInputBox(): VNode {
        return m( Tag.input ) {
            attributes(
                object {
                    val style = object {
                        val fontFamily = "inherit"
                    }
                    val type = "text"
                    val value = adminController.proposedNewCashHoldings( player )
                }
            )
            eventHandlers {
                onInputExt = { event ->
                    adminController.recordProposedNewCashHoldings(
                        player, ( event.target as HTMLInputElement ).value
                    )
                }
            }
        }
    }

    private fun cashToggleUpdateModeButton(): VNode {
        return m( Tag.button ) {
            content( "Toggle new cash" )
            eventHandlers { onclick = { adminController.toggleUpdatingCash( player ) } }
        }
    }

    private fun cashUpdateButton(): VNode {
        return m( Tag.button ) {
            content( "Update cash" )
            eventHandlers { onclick = { adminController.updateCashHoldings( player ) } }
        }
    }

}