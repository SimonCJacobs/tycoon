package jacobs.tycoon.application

import jacobs.tycoon.services.Network
import jacobs.tycoon.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Application ( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val network by kodein.instance <Network> ()
    private val view by kodein.instance < View > ()

    fun start() {
        this.coroutineScope.launch { network.connect() }
        this.view.initialise()
    }

}