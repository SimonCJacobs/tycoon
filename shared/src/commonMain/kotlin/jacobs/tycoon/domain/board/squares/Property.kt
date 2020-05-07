package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
abstract class Property : Square() {

    abstract val listPrice: CurrencyAmount

    private var mortgaged: Boolean = false
    var owner: Player? = null

    fun hasOwner(): Boolean {
        return null != this.owner
    }

    fun isMortgaged(): Boolean {
        return null != this.owner
    }

    abstract fun rent() : Int

}