package jacobs.tycoon.controller

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.GameStateProvider
import jacobs.tycoon.network.Network
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainController ( kodein: Kodein ) {

    private val game: Game by kodein.instance()
    private val gameStateProvider: GameStateProvider by kodein.instance()
    private val network: Network by kodein.instance()

    init {
        this.network.registerMessageReceiver( this::incomingMessage )
    }

    fun advanceToMainPage() {
        this.gameStateProvider.stage = GameStage.IN_PLAY
        console.log( "advancing" )
    }

    private fun incomingMessage( message: String ) {
        console.log( "received $message" )
        this.game.message += message
    }

}