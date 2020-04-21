package jacobs.tycoon.domain

class GameStateProvider (
    private val game: Game
) {

    fun message() : String {
        return game.message + " " + game.num
    }

}