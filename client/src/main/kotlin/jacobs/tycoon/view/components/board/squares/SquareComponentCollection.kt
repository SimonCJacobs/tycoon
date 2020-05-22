package jacobs.tycoon.view.components.board.squares

import jacobs.tycoon.domain.board.squares.ActionSquare
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.view.components.board.squares.PropertyComponent
import jacobs.tycoon.view.components.board.squares.SquareComponent

class SquareComponentCollection {
    val actionSquares: MutableMap < ActionSquare, SquareComponent<ActionSquare>> = mutableMapOf()
    var jailComponent: JailComponent? = null
    val stations: MutableMap < Station, PropertyComponent<Station>> = mutableMapOf()
    val streets: MutableMap < Street, PropertyComponent<Street>> = mutableMapOf()
    val utilities: MutableMap < Utility, PropertyComponent<Utility>> = mutableMapOf()
}