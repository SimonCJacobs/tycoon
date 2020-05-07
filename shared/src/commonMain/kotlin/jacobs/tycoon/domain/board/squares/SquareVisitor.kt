package jacobs.tycoon.domain.board.squares

interface SquareVisitor < T > {
    fun visit( square: CardSquare ): T
    fun visit( square: FreeParkingSquare ): T
    fun visit( square: GoSquare ): T
    fun visit( square: GoToJailSquare ): T
    fun visit( square: JailSquare ): T
    fun visit( square: JustVisitingJailSquare ): T
    fun visit( square: Station ): T
    fun visit( square: Street ): T
    fun visit( square: TaxSquare ): T
    fun visit( square: Utility ): T
}