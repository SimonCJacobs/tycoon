package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game

class SignUp(
    private val phasePhactory: PhasePhactory
) : GamePhase() {

    override fun nextPhase( game: Game ): GamePhase {
        return this.phasePhactory.rollingForOrder( game )
    }

}