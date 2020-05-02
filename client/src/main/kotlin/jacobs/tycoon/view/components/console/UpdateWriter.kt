package jacobs.tycoon.view.components.console

import jacobs.tycoon.state.AddPlayer
import jacobs.tycoon.state.GameUpdate
import jacobs.tycoon.state.SetBoard
import jacobs.tycoon.state.SetPieces
import jacobs.tycoon.state.UpdateProcessor
import jacobs.tycoon.state.UpdateStage

class UpdateWriter : UpdateProcessor < String > {

    override fun process( gameUpdate: GameUpdate ): String {
        return when( gameUpdate ){
            is AddPlayer -> "Player ${ gameUpdate.singleArg().name } has joined using piece ${ gameUpdate.singleArg().piece.name }"
            is SetBoard -> "Using board with locations in ${ gameUpdate.singleArg().location }"
            is SetPieces -> "Using \"${ gameUpdate.singleArg().name }\" piece set"
            is UpdateStage -> "Game entered ${ gameUpdate.singleArg().name } phase"
        }
    }

}