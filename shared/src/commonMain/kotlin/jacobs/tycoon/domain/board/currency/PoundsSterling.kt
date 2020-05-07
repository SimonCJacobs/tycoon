package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.Serializable

@Serializable
class PoundsSterling : Currency() {

    override val prefix: String= "£"

}