package jacobs.tycoon.services

import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.domain.actions.RollTheDice
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.players.Position
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ActionWriter( kodein: Kodein ) : ActionProcessor < String > {

    private val gameName by kodein.instance < String > ( tag = "gameName" )
    private val gameState by kodein.instance < GameState > ()

    override fun process( gameAction: GameAction ): String? {
        if ( false == gameAction.successful )
            return null
        return when( gameAction ){
            is AddPlayer -> "Player ${ gameAction.playerName } has joined using piece " +
                gameAction.playingPiece.name
            is CompleteSignUp -> "The game sign-up stage is complete. Let's roll the dice to see the order of play :)"
            is NewGame -> "A new game of $gameName has begun!"
            is RollTheDice -> "${ gameAction.position.name() } rolled a ${ gameAction.diceRoll.result }"
            is SetBoard -> "Using board with locations in ${ gameAction.board.location }"
            is SetPieces -> "Using \"${ gameAction.pieceSet.name }\" piece set"
        }
    }

    private fun Position.name(): String {
        return this.getPlayer( gameState.game() ).name
    }

}