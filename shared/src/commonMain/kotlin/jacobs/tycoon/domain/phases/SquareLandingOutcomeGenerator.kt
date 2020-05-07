package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.MoveOutcome
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.FreeParkingSquare
import jacobs.tycoon.domain.board.squares.GoSquare
import jacobs.tycoon.domain.board.squares.GoToJailSquare
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.JustVisitingJailSquare
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.SquareVisitor
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.TaxSquare
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.domain.players.Player

class SquareLandingOutcomeGenerator (
    private val game: Game,
    private val playerWithTurn: Player,
    private val phasePhactory: PhasePhactory
) : SquareVisitor < Unit > {

    lateinit var outcome: MoveOutcome
    lateinit var nextPhase: GamePhase

    override fun visit( square: CardSquare ) {
        this.nextPhase = CardTurnover( this.playerWithTurn, square )
    }

    override fun visit( square: FreeParkingSquare ) {
        this.doNewRollingPhase()
        this.outcome = MoveOutcome.FREE_PARKING
    }

    override fun visit( square: GoSquare ) {
        this.doNewRollingPhase()
        this.outcome = MoveOutcome.FREE_PARKING
    }

    override fun visit( square: GoToJailSquare ) {
        this.doNewRollingPhase()
        this.outcome = MoveOutcome.GO_TO_JAIL
    }

    override fun visit( square: JailSquare ) {
        throw Error( "Not possible to reach jail through a throw of the dice" )
    }

    override fun visit( square: JustVisitingJailSquare ) {
        this.doNewRollingPhase()
        this.outcome = MoveOutcome.JUST_VISITING
    }

    override fun visit( square: Station ) {
        this.doPropertyMoveOutcome( square )
    }

    override fun visit( square: Street ) {
        this.doPropertyMoveOutcome( square )
    }

    override fun visit( square: TaxSquare ) {
        playerWithTurn.debitFunds( square.getTaxCharge() )
        this.doNewRollingPhase()
        this.outcome = MoveOutcome.TAX
    }

    override fun visit( square: Utility ) {
        this.doPropertyMoveOutcome( square )
    }

    private fun doNewRollingPhase() {
        this.nextPhase = this.phasePhactory.rollingForMove(
            this.nextPlayerInTurn()
        )
    }

    private fun doPropertyMoveOutcome( property: Property ) {
        when {
            property.hasOwner() && property.isMortgaged() -> {
                this.doNewRollingPhase()
                this.outcome = MoveOutcome.ON_MORTGAGED_PROPERTY
            }
            property.hasOwner() -> {
                this.nextPhase = PotentialRentCharge( this.playerWithTurn, property )
                this.outcome = MoveOutcome.POTENTIAL_RENT
            }
            else -> {
                this.nextPhase = PotentialPurchase( this.playerWithTurn, property )
                this.outcome = MoveOutcome.POTENTIAL_PURCHASE
            }
        }
    }

    private fun nextPlayerInTurn(): Player {
        return game.players.next( this.playerWithTurn )
    }

}