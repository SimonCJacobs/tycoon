package jacobs.tycoon.view.components.board

import jacobs.tycoon.domain.board.ActionSquare

class ActionSquareComponent( private val actionSquare: ActionSquare )
    : SquareComponent( actionSquare.name ) {

}