package jacobs.websockets

import kotlinx.serialization.Serializable

@Serializable
data class SocketId ( val index: Int ) {

    companion object {
        val NULL = SocketId( -1 )
    }

}