package jacobs.tycoon.domain.board

class Street( name: String, listPrice: Int ) : Property( name, listPrice ) {

    override fun < T > accept( squareVisitor: SquareVisitor<T>): T {
        return squareVisitor.visit( this )
    }

    override fun rent(): Int {
        TODO("Not yet implemented")
    }

}