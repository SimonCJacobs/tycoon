package jacobs.tycoon.application

import jacobs.tycoon.network.Network
import jacobs.tycoon.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Application ( kodein: Kodein ) {

    private val coroutineScope: CoroutineScope by kodein.instance()
    private val network: Network by kodein.instance()
    private val view: View by kodein.instance()

    fun start() {
        this.view.initialise()
        this.coroutineScope.launch { network.connect() }
    }

}