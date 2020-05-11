package jacobs.tycoon.domain.board.squares

interface PropertyVisitor < T > {
    fun visit( square: Station ): T
    fun visit( square: Street ): T
    fun visit( square: Utility ): T
}