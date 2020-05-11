package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import org.js.mithril.VNode
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.MainPageController
import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.board.DiceComponent
import jacobs.tycoon.view.components.console.Console
import jacobs.tycoon.view.components.players.PlayerComponentFactory
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainPage ( kodein: Kodein ) : Page {

    private val boardComponent by kodein.instance < BoardComponent > ()
    private val diceComponent by kodein.instance < DiceComponent > ()
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
                getActionPanel(),
                m( gameConsole )
            )
        }
    }

    @Suppress( "unused", "RedundantIf" )
    private fun getStartButton(): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val type = "button"
                val disabled = if ( false == mainPageController.canGameStart() ) true else false
            } )
            eventHandlers {
                onclick = { mainPageController.startGame() }
            }
            content( "Let's get this show on the road :)" )
        }
    }

    @Suppress( "unused" )
    private fun getActionPanel(): VNode {
        return m( Tag.div ) {
            attributes ( object {
                val style = object {
                    val display = "flex"
                    val justifyContent = "space-between"
                }
            } )
            children(
                m( ownPlayerComponent ),
                m( diceComponent )
            )
        }
    }

}