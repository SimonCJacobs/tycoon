package jacobs.tycoon.application

import jacobs.tycoon.state.StateInitialiser
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Application ( kodein: Kodein ) {

    private val socketServer by kodein.instance < SocketServer > ()
    private val stateInitialiser by kodein.instance < StateInitialiser >()
    private val updateEngine by kodein.instance < UpdateEngine > ()

    suspend fun run() {
        this.stateInitialiser.initialiseStandardGame()
        this.socketServer.startServer()
        this.updateEngine.startUpdating()
    }

}