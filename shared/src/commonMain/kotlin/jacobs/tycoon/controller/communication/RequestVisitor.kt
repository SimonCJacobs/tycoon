package jacobs.tycoon.controller.communication

import jacobs.tycoon.controller.communication.application.ApplicationRequest

interface RequestVisitor < T > {
    suspend fun visit( applicationRequest: ApplicationRequest ): T
    suspend fun visit( gameActionRequest: GameActionRequest ): T
}