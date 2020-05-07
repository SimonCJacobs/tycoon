package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
class GoSquare( override val name: String ) : ActionSquare() {
}