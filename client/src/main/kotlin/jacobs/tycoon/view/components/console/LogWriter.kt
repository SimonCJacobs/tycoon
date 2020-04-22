package jacobs.tycoon.view.components.console

import jacobs.tycoon.domain.logs.LogEntry
import jacobs.tycoon.domain.logs.LogProcessor
import jacobs.tycoon.domain.logs.PlayerJoined

class LogWriter : LogProcessor < String > {

    override fun process( logEntry: LogEntry ): String {
        return when( logEntry ){
            is PlayerJoined ->
                "Player ${ logEntry.player.name } joined the game using piece ${ logEntry.player.piece.name }"
        }
    }

}