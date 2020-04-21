package jacobs.tycoon.domain

abstract class Square ( val name: String ) {

    abstract fun < T > accept( squareVisitor: SquareVisitor < T > ): T

}