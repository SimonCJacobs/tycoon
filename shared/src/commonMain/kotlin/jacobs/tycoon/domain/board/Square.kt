package jacobs.tycoon.domain.board

abstract class Square ( val name: String ) {

    abstract fun < T > accept( squareVisitor: SquareVisitor<T>): T

}