package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PlayerFactory ( kodein: Kodein ) {

    private val currency by kodein.instance < Currency > ()
    private val initialCashCount by kodein.instance < Int > ( tag = "initialCashCount" )
    private val toJailDoubleCount by kodein.instance < Int > ( tag = "toJailDoubleCount" )

    fun getNew( name: String, piece: PlayingPiece, position: SeatingPosition ): Player {
        return Player(
            name = name,
            piece = piece,
            position = position,
            cashHoldings = currency.ofAmount( initialCashCount ),
            toJailDoubleCount = toJailDoubleCount
        )
    }

}