package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.Serializable

@Serializable
class NullCurrency : Currency() {
    override val prefix = ""
}
