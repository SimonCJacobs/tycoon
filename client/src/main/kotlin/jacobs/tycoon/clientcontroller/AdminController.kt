package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.AdminState
import jacobs.tycoon.clientstate.AdministratableProperties
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.MiscellaneousRules
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class AdminController ( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val gameRules by kodein.instance < MiscellaneousRules > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()
    private val state by kodein.instance < AdminState > ()

    fun isUpdatingCash( player: Player ): Boolean {
        return playerProperties( player ).updatingCash
    }

    fun proposedNewCashHoldings( player: Player ): String {
        return playerProperties( player ).newCashHoldings
    }

    fun recordNewPlayerIfNecessary( player: Player ) {
        if ( state.players.containsKey( player ) == false )
            state.players.put( player, AdministratableProperties() )
    }

    fun recordProposedNewCashHoldings( player: Player, newCashString: String ) {
        playerProperties( player ).newCashHoldings = newCashString
    }

    fun toggleUpdatingCash( player: Player ) {
        playerProperties( player ).updatingCash = ( playerProperties( player ).updatingCash == false )
    }

    fun updateCashHoldings( player: Player ) {
        val newCashHoldings = gameRules.currency.ofAmount(
            this.playerProperties( player ).newCashHoldings.toInt()
        )
        launch {
            outgoingRequestController.updateCashHoldingsRequest( player, newCashHoldings )
            playerProperties( player ).resetCashSettingProperties()
        }
    }

    private fun playerProperties( player: Player ): AdministratableProperties {
        return this.state.players.getValue( player )
    }

}