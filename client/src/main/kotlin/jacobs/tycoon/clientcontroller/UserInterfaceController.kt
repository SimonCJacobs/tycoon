package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.phases.SignUp
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.services.PlayerIdentifier
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

open class UserInterfaceController( kodein: Kodein ) : CoroutineScope by kodein.direct.instance() {

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()
    private val playerIdentifier by kodein.instance <PlayerIdentifier> ()

    fun getCurrency(): Currency {
        return gameState.game().board.currency
    }

    fun isClientWaitingForServer(): Boolean {
        return this.clientState.isWaitingForServer
    }

    fun hasGameBeenInitialised(): Boolean {
        return this.gameState.hasGame()
    }

    fun isGameUnderway(): Boolean {
        return this.gameState.game().isGameUnderway()
    }

    fun isSignUpPhase(): Boolean {
        return this.gameState.game().isPhase < SignUp > ()
    }

    protected fun isTurnOfOwnPlayer(): Boolean {
        return gameState.game().isTurnOfPlayer(
            playerIdentifier.playerUsingThisMachine
        )
    }

    protected fun ownPlayer(): Player {
        return playerIdentifier.playerUsingThisMachine
    }

    fun userOfClientMachineHasSignedUpForGame(): Boolean {
        return this.playerIdentifier.hasUserOfThisMachineSignedUpForGame()
    }

    protected fun Int.toCurrency(): CurrencyAmount {
        return CurrencyAmount( this, getCurrency() )
    }

}