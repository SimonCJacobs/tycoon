package jacobs.tycoon.domain.logs

class ActionLog {

    fun < T > process( processor: LogProcessor < T > ): List < T > {
        TODO()
    }

    fun isEmpty(): Boolean {
        return true
    }

}