package jacobs.tycoon.controller.communication

interface RequestVisitor < T > {
    suspend fun visit(openActionRequest: OpenActionRequest ): T
    suspend fun visit( positionalActionRequest: PositionalActionRequest ): T
}