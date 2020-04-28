package jacobs.tycoon.view

import jacobs.mithril.m
import jacobs.tycoon.controller.MainController
import jacobs.tycoon.view.components.pages.EntryPage
import jacobs.tycoon.view.components.pages.MainPage
import jacobs.tycoon.view.components.pages.Page
import org.js.mithril.Component
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class PageWrapper( kodein: Kodein ) : Component {

    /**
     * Note that Kodein's lazy loading can't be used on components: it seems that doing
     * so puts the properties on the JavaScript prototype so they are regenerated during
     * the Mithril DOM cycle
     */
    private val mainController by kodein.instance < MainController > ()

    private val entryPage by kodein.instance < EntryPage > ()
    private val mainPage by kodein.instance < MainPage > ()

    override fun view(): VNode {
        return m( this.getCurrentPage() )
    }

    private fun getCurrentPage(): Page {
        return when( this.mainController.getViewStage() ) {
            ViewState.SIGN_UP -> this.entryPage
            ViewState.PLAYING_AREA -> this.mainPage
        }
    }

}