package jacobs.tycoon.controller.communication

interface RequestVisitor < T > {
    suspend fun visit( addPlayerRequest: AddPlayerRequest ): T
    suspend fun visit( simpleRequestWrapper: SimpleRequestWrapper ): T
}