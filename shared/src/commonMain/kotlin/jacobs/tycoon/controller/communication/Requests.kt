package jacobs.tycoon.controller.communication

import kotlinx.serialization.Serializable

@Serializable
class SimpleRequestWrapper(
    val identifier: SimpleRequest
) : Request {
    override fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }
}