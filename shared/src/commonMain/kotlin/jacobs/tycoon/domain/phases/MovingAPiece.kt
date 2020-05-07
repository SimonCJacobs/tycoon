package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.players.Player

class MovingAPiece(
    override val playerWithTurn: Player,
    val destinationSquare: Square,
    private val goCreditAmount: CurrencyAmount,
    private val phasePhactory: PhasePhactory
) : TurnBasedPhase() {

    private var didPassGo: Boolean = false
    private lateinit var outcomeGenerator: SquareLandingOutcomeGenerator

    fun isSquareDropTarget( square: Square): Boolean {
        return this.destinationSquare == square
    }

    override fun nextPhase( game: Game ): GamePhase {
        return this.outcomeGenerator.nextPhase
    }

    fun processOutcome( game: Game ) {
        this.didPassGo = this.playerWithTurn.moveToSquareAndMaybePassGo( this.destinationSquare )
        this.dealWithAnyPassingOfGo( didPassGo )
        this.generateOutcomeOfLandingOnSquare( game )
    }

    fun getMoveResult(): MoveResult {
        return MoveResult(
            destinationSquare = destinationSquare,
            outcome = this.outcomeGenerator.outcome,
            didPassGo = this.didPassGo
        )
    }

    private fun dealWithAnyPassingOfGo( didPassGo: Boolean ) {
        if ( didPassGo )
            this.playerWithTurn.creditFunds( this.goCreditAmount )
    }

    private fun generateOutcomeOfLandingOnSquare( game: Game ) {
        this.outcomeGenerator = SquareLandingOutcomeGenerator( game, playerWithTurn, phasePhactory )
        this.destinationSquare.accept( this.outcomeGenerator )
    }

}