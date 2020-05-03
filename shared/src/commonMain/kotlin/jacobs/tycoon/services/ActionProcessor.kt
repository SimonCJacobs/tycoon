package jacobs.tycoon.services

import jacobs.tycoon.domain.actions.GameAction

interface ActionProcessor < T > {
    fun process(gameAction: GameAction): T
}