package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.controller.MainController
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class EntryPage( kodein: Kodein ) : Page {

    private val mainController: MainController by kodein.instance()

    override fun view(): VNode {
        return m( Tag.div ) {
            child( m( Tag.button ) {
                attributes { onclick = mainController::advanceToMainPage }
                content( "click to advance" )
            } )
        }
    }

}