package jacobs.tycoon.domain.board

class ActionSquare ( name: String ) : Square( name ) {

    override fun < T > accept( squareVisitor: SquareVisitor<T>): T {
        return squareVisitor.visit( this )
    }

}