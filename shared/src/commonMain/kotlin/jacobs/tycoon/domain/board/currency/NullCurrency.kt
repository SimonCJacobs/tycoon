package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.Serializable

@Serializable
class NullCurrency : Currency() {
    override val inWords: String
        get() = throw Error( "Null currency not to be used" )
    override val prefix: String
        get() = throw Error( "Null currency not to be used" )
}
