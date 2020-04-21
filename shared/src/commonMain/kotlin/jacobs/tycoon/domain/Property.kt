package jacobs.tycoon.domain

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