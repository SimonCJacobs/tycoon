package jacobs.tycoon.domain.logs

class ActionLog {

    private val entries: MutableList < LogEntry > = mutableListOf()

    fun log( entry: LogEntry ) {
        this.entries.add( entry )
    }

    fun < T > process( processor: LogProcessor < T > ): List < T > {
        return this.entries.map { processor.process( it ) }
    }

    fun isEmpty(): Boolean {
        return this.entries.isEmpty()
    }

}