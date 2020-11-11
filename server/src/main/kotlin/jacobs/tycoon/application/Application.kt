package jacobs.tycoon.application

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.GameExecutor
import jacobs.tycoon.domain.GameInitialiser
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Application ( kodein: Kodein ) {

    private val gameController by kodein.instance < GameController > ()
    private val gameExecutorWrapper by kodein.instance < GameExecutor > ( tag = "wrapper" )
    private val gameInitialiser by kodein.instance < GameInitialiser > ()
    private val socketServer by kodein.instance < SocketServer > ()
    private val updateEngine by kodein.instance < UpdateEngine > ()

    suspend fun run() {
        this.gameController.initialise()  // Completes the dependency chain
        this.gameInitialiser.initialiseStandardGame( gameExecutorWrapper )
        this.socketServer.startServer()
        this.updateEngine.startUpdating()
    }

}