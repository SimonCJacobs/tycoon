package jacobs.tycoon.view.components.players

import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PlayerComponentFactory( kodein: Kodein ) {

    private val gameState by kodein.instance < GameState > ()

    fun getSinglePlayerComponent( player: Player ): SinglePlayerComponent {
        return SinglePlayerComponent( player )
    }

    fun getMultiplePlayersComponentByExclusion( playerToExclude: Player ): MultiplePlayersComponent {
        return MultiplePlayersComponent( playerToExclude, this )
    }

    fun getSinglePlayerComponentsExcluding( playerToExclude: Player ): List < SinglePlayerComponent > {
        return this.gameState.game().players.allExcept( playerToExclude )
            .map { getSinglePlayerComponent( it ) }
    }

}