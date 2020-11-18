package jacobs.tycoon.clientcontroller

import jacobs.tycoon.application.ApplicationMode
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.phases.SignUp
import jacobs.tycoon.services.PlayerIdentifier
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

open class UserInterfaceController ( kodein: Kodein ) : CoroutineScope by kodein.direct.instance() {

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()
    private val mode by kodein.instance < ApplicationMode > ()
    protected val playerIdentifier by kodein.instance < PlayerIdentifier > ()

    fun getCurrency(): Currency {
        return gameState.game().board.currency
    }

    fun isClientWaitingForServer(): Boolean {
        return this.clientState.isWaitingForServer
    }

    fun hasGameBeenInitialised(): Boolean {
        return this.gameState.hasGame()
    }

    fun isAdminMode(): Boolean {
        return this.mode == ApplicationMode.ADMIN
    }

    fun isAuthorisedToAdminstrate(): Boolean {
        return this.isAdminMode() && this.clientState.authorisedToAdministrate
    }

    fun isGameUnderway(): Boolean {
        return this.gameState.game().isGameUnderway()
    }

    fun isSignUpPhase(): Boolean {
        return this.gameState.game().isPhase < SignUp > ()
    }

    protected fun isTurnOfOwnPlayer(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse {
            gameState.game().isTurnOfPlayer( it )
        }
    }

    fun userOfClientMachineHasSignedUpForGame(): Boolean {
        return this.playerIdentifier.hasUserOfThisMachineSignedUpForGame()
    }

    protected fun Int.toCurrency(): CurrencyAmount {
        return CurrencyAmount( this, getCurrency() )
    }

}