package jacobs.tycoon.view

import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.UserInterfaceController
import jacobs.tycoon.view.components.pages.EntryPage
import jacobs.tycoon.view.components.pages.MainPage
import jacobs.tycoon.view.components.pages.Page
import org.js.mithril.Component
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PageWrapper( kodein: Kodein ) : Component {

    private val uiController by kodein.instance < UserInterfaceController > ()

    private val entryPage by kodein.instance < EntryPage > ()
    private val mainPage by kodein.instance < MainPage > ()

    override fun view(): VNode {
        return m( this.getCurrentPage() )
    }

    private fun getCurrentPage(): Page {
        return when( this.uiController.getViewStage() ) {
            ViewState.SIGN_UP -> this.entryPage
            ViewState.PLAYING_AREA -> this.mainPage
        }
    }

}