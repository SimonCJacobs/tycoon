package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.phases.results.MoveOutcome
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
import jacobs.tycoon.domain.phases.rent.PotentialRentCharge
import jacobs.tycoon.domain.players.Player

class SquareLandingOutcomeGenerator (
    private val game: Game,
    private val playerWithTurn: Player
) : SquareVisitor < Unit > {

    private var maybeNextPhaseLambda: ( PhasePhactory.( Player ) -> TurnBasedPhase )? = null
    var maybePotentialRentCharge: PotentialRentCharge? = null

    val isNextPhase: Boolean
        get() = null != maybeNextPhaseLambda

    /**
     * The [Player] in the lambda is the player whose turn it is next
     */
    val nextPhaseLambda: PhasePhactory.( Player ) -> TurnBasedPhase
        get() = maybeNextPhaseLambda ?: throw Error ( "No implied next phase from square landing" )

    lateinit var outcome: MoveOutcome

    override fun visit( square: CardSquare ) {
        this.maybeNextPhaseLambda = { this.cardReading( playerWithTurn, square ) }
        this.outcome = MoveOutcome.CARD_READING
    }

    override fun visit( square: FreeParkingSquare ) {
        this.outcome = MoveOutcome.FREE_PARKING
    }

    override fun visit( square: GoSquare ) {
        this.outcome = MoveOutcome.ON_GO_SQUARE
    }

    override fun visit( square: GoToJailSquare ) {
        val jailSquare = game.board.jailSquare
        this.maybeNextPhaseLambda = { this.movingAPieceNotViaGo( playerWithTurn, jailSquare ) }
        this.outcome = MoveOutcome.GO_TO_JAIL
    }

    override fun visit( square: JailSquare ) {
        this.outcome = MoveOutcome.JAIL
    }

    override fun visit( square: JustVisitingJailSquare ) {
        this.outcome = MoveOutcome.JUST_VISITING
    }

    override fun visit( square: Station ) {
        this.doPropertyMoveOutcome( square )
    }

    override fun visit( square: Street ) {
        this.doPropertyMoveOutcome( square )
    }

    override fun visit( square: TaxSquare ) {
        this.outcome = MoveOutcome.TAX
        this.maybeNextPhaseLambda = {
            paymentDueToBank(
                playerWithTurn = playerWithTurn,
                playerOwingMoney = playerWithTurn,
                amountDue = square.getTaxCharge(),
                reason = "tax"
            )
        }
    }

    override fun visit( square: Utility ) {
        this.doPropertyMoveOutcome( square )
    }

    private fun doPropertyMoveOutcome( property: Property ) {
        when {
             property.isMortgaged() -> {
                this.outcome = MoveOutcome.ON_MORTGAGED_PROPERTY
            }
            this.playerWithTurn.owns( property ) -> {
                this.outcome = MoveOutcome.ON_OWN_PROPERTY
            }
            this.game.isPropertyOwned( property ) -> {
                this.maybePotentialRentCharge = PotentialRentCharge( playerWithTurn, property )
                this.outcome = MoveOutcome.POTENTIAL_RENT
            }
            else -> {
                this.maybeNextPhaseLambda = { potentialPurchase( playerWithTurn, property ) }
                this.outcome = MoveOutcome.POTENTIAL_PURCHASE
            }
        }
    }

}