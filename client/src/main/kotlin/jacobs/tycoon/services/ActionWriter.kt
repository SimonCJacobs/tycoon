package jacobs.tycoon.services

import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ActionWriter( kodein: Kodein ) : ActionProcessor < String > {

    private val gameName by kodein.instance < String > ( tag = "gameName" )

    override fun process( gameAction: GameAction ): String {
        return when( gameAction ){
            is AddPlayer -> "Player ${ gameAction.argPair().first } has joined using piece ${ gameAction.argPair().second.name }"
            is CompleteSignUp -> "The game sign-up stage is complete. Let's roll the dice to see the order of play :)"
            is NewGame -> "A new game of $gameName has begun!"
            is SetBoard -> "Using board with locations in ${ gameAction.singleArg().location }"
            is SetPieces -> "Using \"${ gameAction.singleArg().name }\" piece set"
        }
    }

}