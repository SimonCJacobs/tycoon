package jacobs.websockets.engine

internal interface CommunicationVisitor {
    fun visit( notification: Notification )
    fun visit( response: Response)
    fun visit( request: Request )
}