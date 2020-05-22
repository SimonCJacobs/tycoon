package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import org.js.mithril.Component
import org.js.mithril.VNode
import org.w3c.dom.HTMLInputElement

class TradeCashAndCardsForm(
    private val dealController: DealController
) : Component {

    override fun view(): VNode {
        return m( Tag.form ) {
            children(
                getCashOffered(),
                getGetOutOfJailFreeCardsOffered(),
                getCashWanted(),
                getGetOutOfJailFreeCardsWanted()
            )
        }
    }

    private fun getCashOffered(): VNode {
        return cashChoice(
            "Cash offered",
            { dealController.getCashOffered() },
            { dealController.updateCashOffered( it ) }
        )
    }

    private fun getGetOutOfJailFreeCardsOffered(): VNode {
        return getOutOfJailFreeCardsChoice(
            "Get out of jail free cards offered",
            getDataLambda = { dealController.getGetOutOfJailFreeCardsOffered() },
            saveDataLambda = { dealController.updateGetOutOfJailFreeCardsOffered( it ) },
            maxCardsLambda = { dealController.getMaxCardsCanOffer() }
        )
    }

    private fun getCashWanted(): VNode {
        return cashChoice(
            "Cash wanted",
            { dealController.getCashWanted() },
            { dealController.updateCashWanted( it ) }
        )
    }

    private fun getGetOutOfJailFreeCardsWanted(): VNode {
        return getOutOfJailFreeCardsChoice(
            "Get out of jail free cards wanted",
            getDataLambda = { dealController.getGetOutOfJailFreeCardsWanted() },
            saveDataLambda = { dealController.updateGetOutOfJailFreeCardsWanted( it ) },
            maxCardsLambda = { dealController.getMaxCardsCanWant() }
        )
    }

    @Suppress( "unused" )
    private fun cashChoice(
        description: String,
        getDataLambda: () -> String,
        saveDataLambda: ( String ) -> Unit
    ): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.label ) {
                    attributes( object {
                        val `for` = description.removeSpaces()
                    } )
                    content( description + " " + dealController.getCurrency().prefix )
                },
                m( Tag.input ) {
                    attributes( object {
                        val id = description.removeSpaces()
                        val type = "text"
                        val value = getDataLambda()
                    } )
                    eventHandlers {
                        onInputExt = { event -> saveDataLambda( ( event.target as HTMLInputElement ).value ) }
                    }
                }
            )
        }
    }

    @Suppress( "unused" )
    private fun getOutOfJailFreeCardsChoice(
        description: String,
        getDataLambda: () -> Int,
        saveDataLambda: ( Double ) -> Unit,
        maxCardsLambda: () -> Int
    ): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.label ) {
                    attributes( object {
                        val `for` = description.removeSpaces()
                    } )
                    content( description )
                },
                m( Tag.input ) {
                    attributes( object {
                        val id = description.removeSpaces()
                        val max = maxCardsLambda()
                        val min = 0
                        val type = "number"
                        val value = getDataLambda()
                    } )
                    eventHandlers {
                        onInputExt = { event -> saveDataLambda( ( event.target as HTMLInputElement ).valueAsNumber ) }
                    }
                }

            )
        }
    }

}