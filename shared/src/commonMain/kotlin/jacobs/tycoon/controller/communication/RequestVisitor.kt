package jacobs.tycoon.controller.communication

interface RequestVisitor < T > {
    suspend fun visit( actionRequest: ActionRequest ): T
}