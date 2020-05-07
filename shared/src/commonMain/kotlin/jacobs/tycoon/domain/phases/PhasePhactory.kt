package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PhasePhactory( kodein: Kodein ) {

    private val currency by kodein.instance < Currency > ()
    private val goCreditAmount by kodein.instance < Int > ( tag = "goCreditAmount" )

    fun continueRollingForOrder( rollResults: MutableMap < Player, DiceRoll? > ): RollingForOrder {
        return RollingForOrder(
            playerWithTurn = rollResults.entries.first { it.value == null }.key,
            rollResults = rollResults,
            phasePhactory = this
        )
    }

    fun movingAPiece( rollingForMove: RollingForMove, destinationSquare: Square ): MovingAPiece {
        return MovingAPiece(
            playerWithTurn = rollingForMove.playerWithTurn,
            destinationSquare = destinationSquare,
            goCreditAmount =  currency.ofAmount( goCreditAmount ),
            phasePhactory = this
        )
    }

    fun rollingForMove( playerWithTurn: Player ): RollingForMove {
        return RollingForMove(
            playerWithTurn = playerWithTurn,
            phasePhactory = this
        )
    }

    fun rollingForOrder( game: Game ): RollingForOrder {
        return this.rollForOrderGivenPlayers( game.players.asSortedList() )
    }

    fun rollOffAmongstPlayers( players: Set < Player > ): RollingForOrder {
        return this.rollForOrderGivenPlayers( players = players.sorted() )
    }

    private fun rollForOrderGivenPlayers( players: List < Player > )
            : RollingForOrder {
        return RollingForOrder(
            playerWithTurn = players.first(),
            rollResults = players.associateWith { null }.toMutableMap(),
            phasePhactory = this
        )
    }

}