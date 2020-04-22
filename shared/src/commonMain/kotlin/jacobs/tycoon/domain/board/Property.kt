package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.Player

abstract class Property (
    name: String,
    val listPrice: Int,
    var owner: Player? = null
) : Square( name ) {

    var mortgaged: Boolean = false

    fun hasOwner(): Boolean {
        return null != this.owner
    }

    abstract fun rent() : Int

}