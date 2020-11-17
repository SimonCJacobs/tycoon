package jacobs.tycoon.application

import jacobs.tycoon.clientstate.clientStateModule
import jacobs.tycoon.clientcontroller.clientControllerModule
import jacobs.tycoon.clientsettings.clientSettingsModule
import jacobs.tycoon.domain.clientDomainModule
import jacobs.tycoon.domain.domainModule
import jacobs.tycoon.services.servicesModule
import jacobs.tycoon.settings.settingsModule
import jacobs.tycoon.state.sharedStateModule
import jacobs.tycoon.view.viewModule
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance
import org.kodein.di.erased.on
import kotlin.browser.window

class ApplicationBootstrapper {

    companion object {
        private const val ADMIN_PATH = "admin.html"
    }

    fun bootstrap() {
        this.createApplication( getApplicationMode() )
            .start()
    }

    private fun createApplication( mode: ApplicationMode ): Application {
        val kodein = Kodein {
            import( applicationModule( mode ) )
            import( clientDomainModule() )
            import( clientSettingsModule() )
            import( clientStateModule )
            import( clientControllerModule( mode ) )
            import( domainModule() )
            import( servicesModule() )
            import( settingsModule() )
            import( sharedStateModule() )
            import( viewModule( mode ) )
        }
        return kodein.direct.instance()
    }

    private fun getApplicationMode(): ApplicationMode {
        return if ( window.location.href.contains( ADMIN_PATH ) )
            ApplicationMode.ADMIN
        else
            ApplicationMode.NORMAL
    }

}