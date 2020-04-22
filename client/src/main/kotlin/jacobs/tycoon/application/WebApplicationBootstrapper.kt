package jacobs.tycoon.application

import jacobs.tycoon.clientstate.clientStateModule
import jacobs.tycoon.controller.controllerModule
import jacobs.tycoon.domain.domainModule
import jacobs.tycoon.network.networkModule
import jacobs.tycoon.state.stateModule
import jacobs.tycoon.view.viewModule
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class WebApplicationBootstrapper {

    fun bootstrap() {
        this.createApplication()
            .start()
    }

    private fun createApplication(): Application {
        val kodein = Kodein {
            import( applicationModule )
            import( clientStateModule )
            import( controllerModule )
            import( domainModule )
            import( networkModule )
            import( stateModule )
            import( viewModule )
        }
        return kodein.direct.instance()
    }

}