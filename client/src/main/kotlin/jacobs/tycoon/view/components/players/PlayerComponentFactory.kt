package jacobs.tycoon.view.components.players

import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

class PlayerComponentFactory( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState >()
    private val controllerProvider: ( Player ) -> PlayerActionController
        by kodein.factory < Player, PlayerActionController  >()
    private val gameState by kodein.instance < GameState > ()
    private val pieceDisplayStrategy by kodein.instance < PieceDisplayStrategy > ()


    fun getSinglePlayerComponent( player: Player ): SinglePlayerComponent {
        if ( this.isThisThePlayersMachine( player ) )
            return ActiveSinglePlayerComponent(
                gameState, pieceDisplayStrategy, controllerProvider( player ), player
            )
        else
            return PassiveSinglePlayerComponent(
                gameState, pieceDisplayStrategy, controllerProvider( player ), player
            )
    }

    fun getOrderedSinglePlayerComponentsExcluding( playerToExclude: Player ): List < SinglePlayerComponent > {
        return this.gameState.game().players.playersToLeftExcluding( playerToExclude )
            .map { getSinglePlayerComponent( it ) }
    }

    fun getMultiplePlayersComponentByExclusion( playerToExclude: Player ): MultiplePlayersComponent {
        return MultiplePlayersComponent( playerToExclude, this )
    }

    private fun isThisThePlayersMachine( player: Player ): Boolean {
        return player.isPosition( clientState.socket.toPosition() )
    }

}