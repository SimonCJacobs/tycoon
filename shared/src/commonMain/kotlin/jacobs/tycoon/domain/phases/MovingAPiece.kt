package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.players.Player

class MovingAPiece (
    override val playerWithTurn: Player,
    val destinationSquare: Square,
    private val goCreditAmount: CurrencyAmount
) : TurnBasedPhase {

    private var didPassGo: Boolean = false

    lateinit var outcomeGenerator: SquareLandingOutcomeGenerator
    lateinit var result: MoveResult

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun carryOutMove( game: Game ): MoveResult {
        this.didPassGo = this.playerWithTurn.moveToSquareAndMaybePassGo( this.destinationSquare )
        this.dealWithAnyPassingOfGo( didPassGo )
        this.generateOutcomeOfLandingOnSquare( game )
        return this.getMoveResult()
            .also { this.result = it }
    }

    fun isSquareDropTarget( square: Square): Boolean {
        return this.destinationSquare == square
    }

    private fun getMoveResult(): MoveResult {
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
        this.outcomeGenerator = SquareLandingOutcomeGenerator( game, playerWithTurn )
        this.destinationSquare.accept( this.outcomeGenerator )
    }

}