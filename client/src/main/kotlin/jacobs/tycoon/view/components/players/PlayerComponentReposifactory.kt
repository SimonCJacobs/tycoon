package jacobs.tycoon.view.components.players

import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.services.PlayerIdentifier
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.kodein.di.Kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

open class PlayerComponentReposifactory( kodein: Kodein ) {

    protected val controllerProvider: ( Player ) -> PlayerActionController
        by kodein.factory < Player, PlayerActionController > ()
    protected val gameState by kodein.instance < GameState > ()
    protected val pieceDisplayStrategy by kodein.instance < PieceDisplayStrategy > ()
    private val playerIdentifier by kodein.instance < PlayerIdentifier > ()

    private val playerComponents: MutableMap < Player, SinglePlayerComponent > = mutableMapOf()

    fun getSinglePlayerComponent( player: Player ): SinglePlayerComponent {
        return if ( playerComponents.containsKey( player ) )
            playerComponents.getValue( player )
        else
            playerIdentifier.doIfGivenPlayerOnThisMachineOrElse(
                player,
                { newActiveSinglePlayerComponent( it ) },
                { newPassiveSinglePlayerComponent( it ) }
            )
                .also {
                    playerComponents.set( player, it )
                }
    }

    fun getMultiplePlayersComponentsExcludingPresent(): MultiplePlayersComponent {
        return MultiplePlayersComponent( this )
    }

    fun getMultiplePlayerSubComponents(): List < SinglePlayerComponent > {
        return this.playerIdentifier.mapOtherPlayers {
            player -> getSinglePlayerComponent( player )
        }
    }

    private fun newActiveSinglePlayerComponent( player: Player ): ActiveSinglePlayerComponent {
        return ActiveSinglePlayerComponent(
            gameState, pieceDisplayStrategy, controllerProvider( player ), player
        )
    }

    protected open fun newPassiveSinglePlayerComponent( player: Player ): PassiveSinglePlayerComponent {
        return PassiveSinglePlayerComponent(
            gameState, pieceDisplayStrategy, controllerProvider( player ), player
        )
    }

}