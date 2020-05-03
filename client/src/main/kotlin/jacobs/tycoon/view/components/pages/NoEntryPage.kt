package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import org.js.mithril.VNode

class NoEntryPage : Page {

    override fun view(): VNode {
        return m( Tag.h2 ) {
            content( "Sorry señor/señorita. The game has already begun")
        }
    }

}
