package jacobs.tycoon.view

import jacobs.mithril.m
import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.view.components.pages.EntryPage
import jacobs.tycoon.view.components.pages.MainPage
import jacobs.tycoon.view.components.pages.Page
import org.js.mithril.Component
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PageWrapper( kodein: Kodein ) : Component {

    private val gameStateProvider: GameStateProvider by kodein.instance()

    private val entryPage: EntryPage by kodein.instance()
    private val mainPage: MainPage by kodein.instance()

    override fun view(): VNode {
        return m( this.getCurrentPage() )
    }

    private fun getCurrentPage(): Page {
        return when( this.gameStateProvider.stage ) {
            GameStage.PLAYER_SIGN_UP -> this.entryPage
            GameStage.IN_PLAY -> this.mainPage
        }
    }

}