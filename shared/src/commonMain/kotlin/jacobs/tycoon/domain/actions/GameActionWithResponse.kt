package jacobs.tycoon.domain.actions

import jacobs.websockets.content.Messageable
import kotlinx.serialization.Serializable

@Serializable
abstract class GameActionWithResponse : GameAction() {
    abstract fun getResponse(): Messageable
}