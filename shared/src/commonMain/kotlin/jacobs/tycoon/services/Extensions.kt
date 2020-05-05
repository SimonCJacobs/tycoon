package jacobs.tycoon.services

fun < T > List < T >.wrapFromExcludingFirstMatch( predicate: ( T ) -> Boolean ): List < T > {
    val indexOfMatch = this.indexOfFirst( predicate )
    return when {
        this.size <= 1 -> emptyList()
        indexOfMatch == 0 -> this.subList( 1, this.size )
        indexOfMatch + 1 == this.size -> this.subList( 0, indexOfMatch )
        else -> this.subList( indexOfMatch + 1, this.size ) + this.subList( 0, indexOfMatch )
    }
}