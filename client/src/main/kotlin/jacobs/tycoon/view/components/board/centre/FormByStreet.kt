package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import jacobs.tycoon.domain.board.squares.Street
import org.js.mithril.Component
import org.js.mithril.VNode
import org.w3c.dom.HTMLInputElement

abstract class FormByStreet : Component {

    protected abstract val title: String
    protected abstract val streetListProvider: () -> List < Street >
    protected abstract val streetStateProvider: ( Street ) -> Int
    protected abstract val streetStateUpdate: ( Street, Double ) -> Unit
    protected abstract val streetMaxProvider: ( Street ) -> Int

    override fun view(): VNode {
        return m( Tag.form ) {
            children(
                m( Tag.h6 ) { content( title ) },
                *getStreetInputList().toTypedArray()
            )
        }
    }

    private fun getStreetInputList(): List < VNode > {
        return streetListProvider().map { getSingleStreetInput( it ) }
    }

    @Suppress( "unused" )
    private fun getSingleStreetInput( street: Street ): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.label ) {
                    attributes( object {
                        val `for` = street.name.removeSpaces()
                    } )
                    content( street.name )
                },
                m( Tag.input ) {
                    attributes( object {
                        val id = street.name.removeSpaces()
                        val min = 0
                        val max = streetMaxProvider( street )
                        val type = "number"
                        val value = streetStateProvider( street )
                    } )
                    eventHandlers {
                        onInputExt = { event ->
                            streetStateUpdate( street, ( event.target as HTMLInputElement ).valueAsNumber )
                        }
                    }
                }
            )
        }
    }


}