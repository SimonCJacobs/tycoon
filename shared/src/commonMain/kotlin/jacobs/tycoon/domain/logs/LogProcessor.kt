package jacobs.tycoon.domain.logs

interface LogProcessor < T > {

    fun process( logEntry: LogEntry ): T

}