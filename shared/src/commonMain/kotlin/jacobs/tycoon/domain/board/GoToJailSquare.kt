package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
class GoToJailSquare : ActionSquare() {

    override val name: String = "Go To Jail"

}