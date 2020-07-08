package jacobs.tycoon.application

import jacobs.tycoon.clientcontroller.ChangeListener
import jacobs.tycoon.domain.GameController
import jacobs.tycoon.services.Network
import jacobs.tycoon.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * TODO list:
 *
 * *** BIG ONE IS TO HAVE A WAY OF AFFECTING GAME AND RESEETING IT ETC
 * **** AND STOPPING NAVIGATION AWAY FROM PAGE
 * **** TO ALLOW GAME TO BE PLAYABLE
 * * Cookie to reestablish connection (by tab?)
 * * Message to ensure cookies enabled (or whatever using)
 * * Warning when not connected
 * * Client attempt to reestablish connection
 * * Add backdoor to alter game state
 * * encrypted communication
 * * malformeed requeest handling
 * * Need some end-to-end tests to make sure serialization has happened correctly on build.
 * *
 * * note when double chick it will crash the game as not in phase for second click if server not responded
 * by that point
 * * DROP NOT PERFECT WHEN PIECE ON SQUARE ALREADDY
 * * NEEED TO CLEARDOUBLE COUNT on visiting jail
 * NEED TO allow leaving jail!
 * * note somwhere xss is covered by mithril
 * * GAME
 * * Trading options
 * * Game winner
 * * Community/chest chance
 * * Game phases
 * * Dice roll
 * * Piece moving
 * * View rules
 * * Mortgaging
 * * Auctions
 * * Go £200
 * * Money
 * * Triple doubles
 * * Jail
 * * GOOJF
 * * Building
 * * Selling
 * * Rent collection
 * * Tax
 * * SHOULD YOU BE ALLOWED TO MORTGAGE DURING AN AUCTION? CAN'T AT PRESENT
 */
class Application ( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val gameController by kodein.instance < GameController > ()
    private val network by kodein.instance < Network > ()
    private val view by kodein.instance < View > ()

    fun start() {
        this.coroutineScope.launch {
            gameController.initialise()
            gameController.startGame()
            network.connect()
            view.initialise()
        }
    }

}