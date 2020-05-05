package jacobs.tycoon.services

import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.domain.actions.RollForOrder
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.actions.results.RollForOrderResultType
import jacobs.tycoon.domain.players.Position
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
        return "${ rollForOrder.actorPosition.name() } rolled a " +
            "${ rollForOrder.result.diceRoll.result }. " + this.getRollForOrderAddendum( rollForOrder )
    }

    private fun getRollForOrderAddendum( rollForOrder: RollForOrder ): String {
        return when( rollForOrder.result.nextPhase ) {
            RollForOrderResultType.ROLLING -> "Let's keep rolling."
            RollForOrderResultType.COMPLETE ->
                "So... ${ rollForOrder.result.winner.name } got the highest and will start the game"
            RollForOrderResultType.ROLL_OFF -> TODO()
        }
    }

    override fun visit( setBoard: SetBoard ): String {
        return "Using board with locations in ${ setBoard.board.location }"
    }

    override fun visit( setPieces: SetPieces ): String {
        return "Using \"${ setPieces.pieceSet.name }\" piece set"
    }

    private fun Position.name(): String {
        return this.getPlayer( gameState.game() ).name
    }

}