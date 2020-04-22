package jacobs.tycoon.view.components

import jacobs.tycoon.domain.ActionSquare

class ActionSquareComponent( private val actionSquare: ActionSquare )
    : SquareComponent( actionSquare.name ) {

}