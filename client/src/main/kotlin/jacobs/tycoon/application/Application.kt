package jacobs.tycoon.application

import jacobs.tycoon.services.Network
import jacobs.tycoon.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * TODO list:
 *
 * * Cookie to reestablish connection
 * * Message to ensure cookies enabled
 * * Warning when not connected
 * * Client attempt to reestablish connection
 * * Add backdoor to alter game state
 * *
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
 * *
 */
class Application ( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val network by kodein.instance <Network> ()
    private val view by kodein.instance < View > ()

    fun start() {
        this.coroutineScope.launch { network.connect() }
        this.view.initialise()
    }

}