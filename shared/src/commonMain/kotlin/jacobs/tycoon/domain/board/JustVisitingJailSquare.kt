package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
class JustVisitingJailSquare : ActionSquare() {

    override val name: String = "Jail, just visiting"

}