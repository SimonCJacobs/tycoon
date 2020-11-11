package jacobs.tycoon.application

import jacobs.tycoon.controller.communication.application.ApplicationAction
import jacobs.tycoon.controller.communication.application.ApplicationExecutor
import jacobs.tycoon.domain.GameExecutor
import jacobs.websockets.SocketId
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ApplicationExecutorImpl ( kodein: Kodein ) : ApplicationExecutor {

    private val adminProperties: AdministrationProperties by kodein.instance < AdministrationProperties > ()
    override val gameExecutor by kodein.instance < GameExecutor > ( tag = "wrapper" )

    private val authorisedSockets: MutableList < SocketId > = mutableListOf()

    override suspend fun execute( applicationAction: ApplicationAction ) {
        if ( applicationAction.requiresAuthorisation && false == this.isAuthorised( applicationAction ) )
            return
        this.executeAuthorisedAction( applicationAction )
    }

    override fun requestAuthorisation( username: String, socketId: SocketId ): Boolean {
        return adminProperties.username == username &&
            this.authorisedSockets.add( socketId )
    }

    private suspend fun executeAuthorisedAction( applicationAction: ApplicationAction ) {
        applicationAction.execute( this )
    }

    private fun isAuthorised( applicationAction: ApplicationAction ): Boolean {
        return this.authorisedSockets.contains( applicationAction.socketId )
    }

}