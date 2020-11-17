package jacobs.tycoon.services

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PlayerIdentifier ( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()

    val maybePlayerUsingThisMachine: Player?
        get() = clientState.seatingPosition?.let { this.getPlayerFromSeatingPosition( it ) }

    fun doIfAPlayerOnThisMachine( playerAction: ( Player ) -> Unit ) {
        maybePlayerUsingThisMachine?.let { playerAction( it ) }
    }

    fun doIfAPlayerOnThisMachineOrFalse( playerAction: ( Player ) -> Boolean ): Boolean {
        return doIfAPlayerOnThisMachine( false, playerAction )
    }

    fun < R > doIfAPlayerOnThisMachine( nonPlayerReturn: R, playerAction: ( Player ) -> R ): R {
        return maybePlayerUsingThisMachine?.let { playerAction( it ) }
            ?: nonPlayerReturn
    }

    fun < R > doIfGivenPlayerOnThisMachineOrElse( player: Player, activePlayerAction: ( Player ) -> R,
                                                  otherPlayerAction: ( Player ) -> R ): R {
        return if ( maybePlayerUsingThisMachine != null && maybePlayerUsingThisMachine == player )
            activePlayerAction( player )
        else
            otherPlayerAction( player )
    }

    fun getPlayerFromSeatingPosition( seatingPosition: SeatingPosition ): Player {
        return gameState.game().players.getPlayerAtPosition( seatingPosition )
    }

    fun hasUserOfThisMachineSignedUpForGame(): Boolean {
        return clientState.seatingPosition != null
    }

    fun < T > mapOtherPlayers( callback: ( Player ) -> T ): List < T > {
        return otherPlayerList().map ( callback )
    }

    private fun otherPlayerList(): List < Player > {
        return maybePlayerUsingThisMachine?.let { gameState.game().players.playersToLeftExcluding( it ) }
            ?: gameState.game().players.activeOrderedList()
    }

}