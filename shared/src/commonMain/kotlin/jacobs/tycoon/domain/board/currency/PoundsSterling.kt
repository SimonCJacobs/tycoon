package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class PoundsSterling : Currency() {

    @Transient override val inWords: String = "pounds"
    @Transient override val prefix: String = "£"

}