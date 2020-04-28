package jacobs.tycoon.view

import jacobs.mithril.Mithril
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.Element

class View ( kodein: Kodein ) {

    private val mainElement by kodein.instance < Element > ( tag = "main" )
    private val pageWrapper by kodein.instance < PageWrapper > ()

    fun initialise() {
        Mithril().mount( this.mainElement, this.pageWrapper )
    }

}