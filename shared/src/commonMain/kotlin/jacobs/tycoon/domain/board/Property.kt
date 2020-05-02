package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
abstract class Property : Square() {

    abstract val listPrice: Int
    var mortgaged: Boolean = false
    var owner: Player? = null

    fun hasOwner(): Boolean {
        return null != this.owner
    }

    abstract fun rent() : Int

}