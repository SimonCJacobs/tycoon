package jacobs.tycoon.controller.communication.application

import kotlinx.serialization.Serializable

@Serializable
class AccessAdminModeRequest ( private val username: String ) : ApplicationAction() {

    override val requiresAuthorisation: Boolean
        get() = false

    override suspend fun execute( applicationExecutor: ApplicationExecutor ) {
        this.successful = applicationExecutor.requestAuthorisation( this.username, this.socketId )
    }

}