package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import jacobs.tycoon.domain.players.Player
import org.js.mithril.VNode

class ComposingTradeComponent(
    override val dealController: DealController,
    override var squaresToASideExcludingCorners: Int
) : ComposingDealComponent() {

    private val tradeCashAndCardsForm: TradeCashAndCardsForm = TradeCashAndCardsForm( dealController )

    companion object {
        private const val PLAYER_CHOICE_NAME = "playerChoice"
    }

    override fun extraDisplay(): VNode {
        return m( Tag.form ) {
            children(
                getChooseCounterparty(),
                m( tradeCashAndCardsForm )
            )
        }
    }

    private fun getChooseCounterparty(): VNode {
        return m( Tag.div ) {
            children (
                m( Tag.h6 ) { content( "Choose whom trading with:" ) },
                *dealController.mapOtherPlayers { getPlayerChoiceRadio( it ) }.toTypedArray()
            )
        }
    }

    @Suppress( "unused" )
    private fun getPlayerChoiceRadio( player: Player ): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.input ) {
                    attributes( object {
                        val checked = dealController.isPlayerIntendingToDealWith( player )
                        val id = player.name.removeSpaces()
                        val name = PLAYER_CHOICE_NAME
                        val type = "radio"
                    } )
                    eventHandlers {
                        onInputExt = { dealController.choosePlayerIntendingToDealWith( player ) }
                    }
                },
                m( Tag.label ) {
                    attributes( object {
                        val `for` = player.name.removeSpaces()
                    } )
                    content( player.name )
                }
            )
        }
    }


    @Suppress( "unused" )
    override fun getDoTheDealButton(): VNode {
        return m( Tag.input ) {
            attributes ( object {
                val type = "submit"
                val value = "Offer trade"
            } )
            eventHandlers {
                onclick = {
                    it.preventDefault()
                    dealController.offerTrade()
                }
            }
        }
    }

}