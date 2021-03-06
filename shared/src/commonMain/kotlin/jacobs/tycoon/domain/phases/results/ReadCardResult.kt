package jacobs.tycoon.domain.phases.results

import kotlinx.serialization.Serializable

@Serializable
data class ReadCardResult (
    val cardText: String
) {
    companion object {
        val NULL: ReadCardResult = ReadCardResult( "" )
    }
}