package jacobs.tycoon.view

import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.UserInterfaceController
import jacobs.tycoon.view.components.pages.AdminPage
import jacobs.tycoon.view.components.pages.EntryPage
import jacobs.tycoon.view.components.pages.MainPage
import jacobs.tycoon.view.components.pages.NoEntryPage
import jacobs.tycoon.view.components.pages.Page
import jacobs.tycoon.view.components.pages.SplashPage
import org.js.mithril.Component
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PageWrapper( kodein: Kodein ) : Component {

    private val uiController by kodein.instance < UserInterfaceController > ()

    private val adminPage by kodein.instance < AdminPage > ()
    private val entryPage by kodein.instance < EntryPage > ()
    private val mainPage by kodein.instance < MainPage > ()
    private val noEntryPage by kodein.instance < NoEntryPage > ()
    private val splashPage by kodein.instance < SplashPage > ()

    override fun view(): VNode {
        return m( this.getCurrentPage() )
    }

    private fun getCurrentPage(): Page {
        return when {
            this.uiController.isAuthorisedToAdminstrate() -> this.adminPage
            this.uiController.isInAdminMode() -> this.entryPage
            this.uiController.hasGameBeenInitialised() == false -> this.splashPage
            this.uiController.userOfClientMachineHasSignedUpForGame() == false && this.uiController.isSignUpPhase()
                -> this.entryPage
            this.uiController.userOfClientMachineHasSignedUpForGame() == false && false == this.uiController.isSignUpPhase()
                -> this.noEntryPage
            else -> this.mainPage
        }
    }

}