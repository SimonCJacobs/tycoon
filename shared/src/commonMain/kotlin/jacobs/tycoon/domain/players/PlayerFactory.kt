package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.rules.MiscellaneousRules
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PlayerFactory ( kodein: Kodein ) {

    private val miscellaneousRules by kodein.instance < MiscellaneousRules > ()

    fun getNew( name: String, piece: PlayingPiece, position: SeatingPosition ): Player {
        return Player(
            name = name,
            piece = piece,
            position = position,
            cashHoldings = miscellaneousRules.initialCashCount
        )
    }

}