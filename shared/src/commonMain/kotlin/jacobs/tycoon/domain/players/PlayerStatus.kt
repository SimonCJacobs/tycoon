package jacobs.tycoon.domain.players

data class PlayerStatus( val position: SeatingPosition ) {
    var isInGame: Boolean = true
}