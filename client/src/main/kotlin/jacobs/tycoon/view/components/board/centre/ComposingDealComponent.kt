package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import jacobs.tycoon.clientstate.DealType
import org.js.mithril.VNode

abstract class ComposingDealComponent : CentreCellComponent() {

    companion object {
        private const val DEAL_TYPE_NAME = "dealType"
    }

    protected abstract val dealController: DealController

    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            children(
                getDealTypeRadios(),
                extraDisplay(),
                getDoTheDealButton(),
                getHideButton()
            )
        }
    }

    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {

        }
    }

    private fun getDealTypeRadios(): VNode {
        return m( Tag.div ) {
            children(
                getSingleDealTypeRadio( "Offer trade", DealType.COMPOSING_TRADE ),
                getSingleDealTypeRadio( "Build houses", DealType.BUILD_HOUSES ),
                getSingleDealTypeRadio( "Sell houses", DealType.SELL_HOUSES ),
                getSingleDealTypeRadio( "Mortgage property", DealType.MORTGAGING ),
                getSingleDealTypeRadio( "Pay off mortgages", DealType.PAYING_OFF_MORTGAGES )
            )
        }
    }

    @Suppress( "unused" )
    private fun getSingleDealTypeRadio(text: String, dealType: DealType ): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.input ) {
                    attributes( object {
                        val checked = dealController.isDealTypeBeingComposed( dealType )
                        val id = dealType
                        val name = DEAL_TYPE_NAME
                        val type = "radio"
                    } )
                    eventHandlers {
                        onInputExt = { dealController.updateDealTypeBeingComposed( dealType ) }
                    }
                },
                m( Tag.label ) {
                    attributes( object {
                        val `for` = dealType
                    } )
                    content( text )
                }
            )
        }
    }

    protected abstract fun extraDisplay(): VNode?
    protected abstract fun getDoTheDealButton(): VNode

    @Suppress( "unused" )
    private fun getHideButton(): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val type = "button"
            } )
            eventHandlers {
                onclick = { dealController.hideComposingDeal() }
            }
            content( "Hide" )
        }
    }

}
