package jacobs.tycoon.view

import jacobs.mithril.Mithril
import org.w3c.dom.Element

class View (
    private val mainElement: Element,
    private val mainPage: MainPage
) {

    fun initialise() {
        Mithril().mount( this.mainElement, this.mainPage )
    }

}