package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import org.js.mithril.VNode
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.MainPageController
import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.console.Console
import jacobs.tycoon.view.components.players.PlayerComponentFactory
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainPage ( kodein: Kodein ) : Page {

    private val boardComponent by kodein.instance < BoardComponent > ()
    private val gameConsole by kodein.instance < Console > ()
    private val mainPageController by kodein.instance < MainPageController > ()
    private val playerComponentFactory by kodein.instance < PlayerComponentFactory > ()

    private val otherPlayersComponent = this.getOtherPlayersComponent()
    private val ownPlayerComponent = this.getOwnPlayerComponent()

    private fun getOtherPlayersComponent(): Component {
        return this.playerComponentFactory.getMultiplePlayersComponentByExclusion(
            this.mainPageController.getOwnPlayer()
        )
    }

    private fun getOwnPlayerComponent(): Component {
        return this.playerComponentFactory.getSinglePlayerComponent(
            this.mainPageController.getOwnPlayer()
        )
    }

    override fun view() : VNode {
        return m( Tag.div ) {
            children(
                m( otherPlayersComponent ),
                m( boardComponent ),
                if ( mainPageController.isSignUpPhase() ) getStartButton() else null,
                m( ownPlayerComponent ),
                m( gameConsole )
            )
        }
    }

    private fun getStartButton(): VNode {
        return m( Tag.button ) {
            attributes {
                button
                if ( false == mainPageController.canGameStart() ) disabled
            }
            eventHandlers {
                onclick = { mainPageController.startGame() }
            }
            content( "Let's get this show on the road :)" )
        }
    }

}