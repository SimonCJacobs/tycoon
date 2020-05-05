package jacobs.tycoon.view.components.players

import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider

class PlayerComponentFactory( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState >()
    private val controllerProvider: ( Player ) -> PlayerActionController
        by kodein.factory < Player, PlayerActionController  >()
    private val gameState by kodein.instance < GameState > ()

    fun getSinglePlayerComponent( player: Player ): SinglePlayerComponent {
        if ( this.isThisThePlayersMachine( player ) )
            return ActiveSinglePlayerComponent( gameState, controllerProvider( player ), player )
        else
            return PassiveSinglePlayerComponent( gameState, controllerProvider( player ), player )
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