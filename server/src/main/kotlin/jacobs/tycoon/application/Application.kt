package jacobs.tycoon.application

import jacobs.tycoon.domain.domainModule
import jacobs.tycoon.servercontroller.serverControllerModule
import jacobs.tycoon.state.stateModule
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

internal class Application {

    suspend fun startAndWaitForConnections() {
        this.setupApplication()
            .direct.instance < SocketListener > ()
            .listenForConnections()
    }

    private fun setupApplication(): Kodein {
        return Kodein {
            import( applicationModule() )
            import( domainModule )
            import( serverControllerModule() )
            import( stateModule )
        }
    }

}