package jacobs.tycoon.domain.logs

abstract class LogEntry {

    abstract fun < T > accept( writer: LogProcessor < T > ): T

}