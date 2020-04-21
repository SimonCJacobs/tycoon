package jacobs.tycoon.view

import jacobs.mithril.Mithril
import jacobs.tycoon.domain.Game
import org.w3c.dom.Element

class View (
    private val mithril: Mithril,
    private val mainElement: Element,
    private val mainPage: MainPage
) {

    fun initialise() {
        this.mithril.mount( this.mainElement, this.mainPage )
    }

}