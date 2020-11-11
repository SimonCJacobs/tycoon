package jacobs.tycoon.clientstate

import jacobs.tycoon.domain.players.Player

class AdminState {
    val players: MutableMap < Player, AdministratableProperties > = mutableMapOf()
}