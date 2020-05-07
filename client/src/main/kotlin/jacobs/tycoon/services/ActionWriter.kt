package jacobs.tycoon.services

import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.domain.actions.PieceMoved
import jacobs.tycoon.domain.actions.RollForMove
import jacobs.tycoon.domain.actions.RollForOrder
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.actions.results.MoveOutcome
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForOrderOutcome
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ActionWriter( kodein: Kodein ) : ActionProcessor < String >, ActionVisitor < String > {

    private val gameName by kodein.instance < String > ( tag = "gameName" )
    private val gameState by kodein.instance < GameState > ()

    override fun process( gameAction: GameAction ): String? {
        if ( false == gameAction.successful )
            return null
        return gameAction.accept( this )
    }

    override fun visit( addPlayer: AddPlayer ): String {
        return "Player ${ addPlayer.playerName } has joined using piece ${ addPlayer.playingPiece.name }"
    }

    override fun visit( completeSignUp: CompleteSignUp ): String {
        return "The game sign-up stage is complete. Let's roll the dice to see the order of play :)"
    }

    override fun visit( newGame: NewGame ): String {
        return "A new game of $gameName has begun!"
    }

    override fun visit( rollForOrder: RollForOrder ): String {
        return "${ rollForOrder.actorPosition.name() } rolled " +
            "${ rollForOrder.result.diceRoll.inWordAndNumber() }. " + this.getRollForOrderAddendum( rollForOrder )
    }

    private fun getRollForOrderAddendum( rollForOrder: RollForOrder ): String {
        return when( rollForOrder.result.nextPhase ) {
            RollForOrderOutcome.ROLLING -> "Let's keep rolling."
            RollForOrderOutcome.COMPLETE ->
                "So... ${ rollForOrder.result.winner.name } got the highest and will start the game"
            RollForOrderOutcome.ROLL_OFF -> "It's a roll off!"
        }
    }

    override fun visit( rollForMove: RollForMove ): String {
        return "${ rollForMove.actorPosition.name() } rolled " +
            "${ rollForMove.result.diceRoll.inWordAndNumber() }. " + this.getRollForMoveAddendum( rollForMove )
    }

    private fun getRollForMoveAddendum( rollForMove: RollForMove ): String {
        return when( rollForMove.result.outcome ) {
            RollForMoveOutcome.GO_TO_JAIL -> "That's a third double in a row... which means some time in the can."
            RollForMoveOutcome.MOVE_TO_SQUARE -> this.getRandomMovingPhrase()
        }
    }

    private fun getRandomMovingPhrase(): String {
        return listOf( "On yer bike!", "Well what are you waiting for?", "Moving time.", "Chop chop",
            "Please proceed to your destination", "Start those engines" ).random()
    }

    override fun visit( pieceMoved: PieceMoved ): String {
        return "Welcome to ${ pieceMoved.result.destinationSquare.name }! " +
            this.getSnippetForMoveOutcome( pieceMoved.result.outcome )
    }

    private fun getSnippetForMoveOutcome( moveOutcome: MoveOutcome ): String {
        return when ( moveOutcome ) {
            MoveOutcome.CARD_TURNOVER -> "Please take a card"
            MoveOutcome.FREE_PARKING -> "Rest a wee while"
            MoveOutcome.GO_TO_JAIL -> "Time to straighten you out."
            MoveOutcome.JUST_VISITING -> "\"Just visiting\", eh?"
            MoveOutcome.ON_MORTGAGED_PROPERTY -> "This property is mortgaged. No charge for this one"
            MoveOutcome.POTENTIAL_RENT -> ""
            MoveOutcome.POTENTIAL_PURCHASE -> "Would you like to buy it?"
            MoveOutcome.TAX -> "You have a bill to pay"
        }
    }

    override fun visit( setBoard: SetBoard ): String {
        return "Using board with locations in ${ setBoard.board.location }"
    }

    override fun visit( setPieces: SetPieces ): String {
        return "Using \"${ setPieces.pieceSet.name }\" piece set"
    }

    private fun SeatingPosition.name(): String {
        return this.getPlayer( gameState.game() ).name
    }

}