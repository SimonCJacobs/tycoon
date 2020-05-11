package jacobs.tycoon.domain.actions.results

import kotlinx.serialization.Serializable

@Serializable
data class ReadCardResult (
    val cardText: String
) {
    companion object {
        val NULL: ReadCardResult = ReadCardResult( "" )
    }
}