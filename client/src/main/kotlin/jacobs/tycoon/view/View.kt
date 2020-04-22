package jacobs.tycoon.view

import jacobs.mithril.Mithril
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.Element

class View ( kodein: Kodein ) {

    private val mainElement: Element by kodein.instance( tag = "main" )
    private val pageWrapper: PageWrapper by kodein.instance()

    fun initialise() {
        Mithril().mount( this.mainElement, this.pageWrapper )
    }

}