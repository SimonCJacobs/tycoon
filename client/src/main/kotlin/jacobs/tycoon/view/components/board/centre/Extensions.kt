package jacobs.tycoon.view.components.board.centre

private val SPACE_AS_CHAR = " ".toCharArray()[ 0 ]

fun String.removeSpaces(): String {
    return this.filter { SPACE_AS_CHAR != it }
}