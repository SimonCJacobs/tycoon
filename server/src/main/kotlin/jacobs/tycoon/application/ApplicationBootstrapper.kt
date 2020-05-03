package jacobs.tycoon.application

import jacobs.tycoon.domain.domainModule
import jacobs.tycoon.servercontroller.serverControllerModule
import jacobs.tycoon.settings.settingsModule
import jacobs.tycoon.domain.serverDomainModule
import jacobs.tycoon.state.sharedStateModule
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

internal class ApplicationBootstrapper {

    suspend fun start() {
        this.getContainer()
            .direct.instance < Application > ()
            .run()
    }

    private fun getContainer(): Kodein {
        return Kodein {
            import( applicationModule() )
            import( domainModule )
            import( serverControllerModule() )
            import(serverDomainModule())
            import( settingsModule() )
            import( sharedStateModule() )
        }
    }

}