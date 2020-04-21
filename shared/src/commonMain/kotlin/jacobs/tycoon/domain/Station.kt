package jacobs.tycoon.domain

class Station ( name: String, listPrice: Int ) : Property ( name, listPrice ) {

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

    override fun rent(): Int {
        TODO("Not yet implemented")
    }


}