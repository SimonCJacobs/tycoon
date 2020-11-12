package jacobs.tycoon.view.components.players

import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.services.PlayerIdentifier
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

class PlayerComponentFactory( kodein: Kodein ) {

    private val controllerProvider: ( Player ) -> PlayerActionController
        by kodein.factory < Player, PlayerActionController > ()
    private val gameState by kodein.instance < GameState > ()
    private val pieceDisplayStrategy by kodein.instance < PieceDisplayStrategy > ()
    private val playerIdentifier by kodein.instance < PlayerIdentifier > ()

    fun getSinglePlayerComponent( player: Player ): SinglePlayerComponent {
        if ( this.playerIdentifier.isPlayerUsingThisMachine( player ) )
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

}