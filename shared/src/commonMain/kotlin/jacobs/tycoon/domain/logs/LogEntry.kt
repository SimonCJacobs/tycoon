package jacobs.tycoon.domain.logs

import jacobs.tycoon.domain.players.Player

sealed class LogEntry

class PlayerJoined( val player: Player ) : LogEntry()
