package jacobs.tycoon.application

import jacobs.tycoon.clientstate.clientStateModule
import jacobs.tycoon.clientcontroller.clientControllerModule
import jacobs.tycoon.domain.domainModule
import jacobs.tycoon.services.servicesModule
import jacobs.tycoon.settings.settingsModule
import jacobs.tycoon.state.sharedStateModule
import jacobs.tycoon.view.viewModule
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class ApplicationBootstrapper {

    fun bootstrap() {
        this.createApplication()
            .start()
    }

    private fun createApplication(): Application {
        val kodein = Kodein {
            import( applicationModule() )
            import( clientStateModule )
            import( clientControllerModule() )
            import( domainModule )
            import( servicesModule() )
            import( settingsModule() )
            import( sharedStateModule() )
            import( viewModule )
        }
        return kodein.direct.instance()
    }

}