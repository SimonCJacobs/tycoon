package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.actions.GameAction

abstract class GameActionRequest (
) : Request {
    abstract val action: GameAction
}