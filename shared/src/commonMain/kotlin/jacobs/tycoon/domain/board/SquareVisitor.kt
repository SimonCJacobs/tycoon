package jacobs.tycoon.domain.board

interface SquareVisitor < T > {
    fun visit( square: ActionSquare): T
    fun visit( station: Station): T
    fun visit( street: Street): T
    fun visit( utility: Utility): T
}