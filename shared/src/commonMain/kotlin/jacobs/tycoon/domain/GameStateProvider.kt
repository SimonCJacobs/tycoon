package jacobs.tycoon.domain

import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameStateProvider ( kodein: Kodein ) {
    private val game: Game by kodein.instance()

    var stage: GameStage = GameStage.PLAYER_SIGN_UP

}