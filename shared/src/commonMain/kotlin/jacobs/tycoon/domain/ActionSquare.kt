package jacobs.tycoon.domain

class ActionSquare ( name: String ) : Square( name ) {

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

}