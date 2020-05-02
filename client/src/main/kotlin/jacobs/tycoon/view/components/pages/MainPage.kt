package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import org.js.mithril.VNode
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.UserInterfaceController
import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.console.Console
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainPage ( kodein: Kodein ) : Page {

    private val board by kodein.instance < BoardComponent > ()
    private val gameConsole by kodein.instance < Console > ()
    private val uiController by kodein.instance < UserInterfaceController > ()

    override fun view() : VNode {
        return m( Tag.div ) {
            children(
                m( board ),
                if ( uiController.isSignUpPhase() ) getStartButton() else null,
                m( gameConsole )
            )
        }
    }

    private fun getStartButton(): VNode {
        return m( Tag.button ) {
            attributes {
                button
                if ( false == uiController.canGameStart() ) disabled
            }
            eventHandlers {
                onclick = { uiController.startGame() }
            }
            content( "Let's get this show on the road :)" )
        }
    }

}