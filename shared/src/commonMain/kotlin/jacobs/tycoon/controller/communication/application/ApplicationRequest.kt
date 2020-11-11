package jacobs.tycoon.controller.communication.application

import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.RequestVisitor
import kotlinx.serialization.Serializable

@Serializable
class ApplicationRequest ( val action: ApplicationAction ) : Request {

    override suspend fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }

}