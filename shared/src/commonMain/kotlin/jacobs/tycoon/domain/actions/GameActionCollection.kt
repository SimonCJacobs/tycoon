package jacobs.tycoon.domain.actions

import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable

@Serializable
class GameActionCollection private constructor() : MessageContent {

    private lateinit var gameActions: List < GameAction >

    companion object {
        fun fromList( gameActionList: List < GameAction > ): GameActionCollection {
            return GameActionCollection()
                .apply { gameActions = gameActionList }
        }
    }

    fun getActions(): List < GameAction > {
        return this.gameActions.toList()
    }

}