package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
class JailSquare : ActionSquare() {

    override val name: String = "Jail"

}