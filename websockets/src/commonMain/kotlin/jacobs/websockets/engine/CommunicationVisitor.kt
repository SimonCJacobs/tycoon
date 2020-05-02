package jacobs.websockets.engine

internal interface CommunicationVisitor {
    suspend fun visit( notification: Notification )
    suspend fun visit( response: Response)
    suspend fun visit( request: Request )
}