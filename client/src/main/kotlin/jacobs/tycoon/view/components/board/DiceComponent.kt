package jacobs.tycoon.view.components.board

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DiceController
import org.js.mithril.Component
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class DiceComponent( kodein: Kodein ) : Component {

    private val diceController by kodein.instance < DiceController > ()

    override fun view(): VNode {
        return m( Tag.div ) {
            children( getContent() )
        }
    }

    private fun getContent(): List < VNode >? {
        if ( false == this.diceController.shouldDiceBeShown() )
            return null
        else
            return this.getDiceNodes()
    }

    private fun getDiceNodes(): List < VNode > {
        return listOf < DiceController.() -> Int > ( { firstDie() }, { secondDie() } )
            .map { getDieNode( it ) }
    }

    private fun getDieNode( faceGetter: DiceController.() -> Int ): VNode {
        return m( Tag.h3 ) {
            content( diceController.faceGetter().toString() )
        }
    }

}