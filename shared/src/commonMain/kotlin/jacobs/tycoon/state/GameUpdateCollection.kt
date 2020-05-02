package jacobs.tycoon.state

import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable

@Serializable
class GameUpdateCollection private constructor() : MessageContent {

    private lateinit var gameUpdates: List < GameUpdate >

    companion object {
        fun fromList( gameUpdateList: List < GameUpdate > ): GameUpdateCollection {
            return GameUpdateCollection()
                .apply { gameUpdates = gameUpdateList }
        }
    }

    fun getUpdates(): List < GameUpdate > {
        return this.gameUpdates.toList()
    }

    fun updateCount(): Int {
        return this.gameUpdates.size
    }

}