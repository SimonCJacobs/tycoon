package jacobs.tycoon.controller

import jacobs.tycoon.domain.Game
import jacobs.tycoon.network.Network

class MainController (
    private val game: Game,
    private val network: Network
) {

    init {
        this.network.registerMessageReceiver( this::incomingMessage )
    }

    private fun incomingMessage( message: String ) {
        console.log( "received $message" )
        this.game.message += message
    }

}