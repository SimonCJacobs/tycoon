package jacobs.tycoon.application

import jacobs.tycoon.network.Network
import jacobs.tycoon.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Application(
    private val coroutineScope: CoroutineScope,
    private val network: Network,
    private val view: View
) {

    fun start() {
        this.view.initialise()
      //  this.coroutineScope.launch { network.connect() }
    }

}