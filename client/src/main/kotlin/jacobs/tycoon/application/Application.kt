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
 *
 * * THERE IS A BUG: auction does not show when chance card is being displayed
 * * ANOTHER SMALL BUG: says "pounds" not pound when sold for £1 in auction
 * * SHOULD PROBABLY HIGHLIGHT SOMEHOW WHOSE TURN IT IS AND WHAT THE NEXT ACTION IS
 * * Trading screen isn't a perfect solution to the problem of showing who owns because takes properties off when
 * ****** they have a house built on them
 *
 * * Auction outcome seems to give three messages in actionlog
 * * Suggests can mortgage with houses on property
 * * NO GAME OPTION FOR MORTGAGE INTEREST PAYMENT ON TRANSFER
 ** ALLOWED CASH TO GO TO ZERO ON PURCHASE, THEN ALLOWED BID OF £1!!
 *
 *
 * * Warning when not connected
 * * Client attempt to reestablish connection
 * * Add backdoor to alter game state
 * * encrypted communication?
 * * malformeed requeest handling
 * * Need some end-to-end tests to make sure serialization has happened correctly on build.
 * * note when double chick it will crash the game as not in phase for second click if server not responded
 * by that point ( is this right?)
 * * DROP NOT PERFECT WHEN PIECE ON SQUARE ALREADDY ( is this right?)
 * * note somehere xss is covered by mithril
 * * Visible what places have houses
 * * Much nicer board
 * * Game winner?
 * * Dice roll graphical?
 * * Piece moving visible to other players?
 * * View rules?
 * * Jail in one place
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