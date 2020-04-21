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

    fun increment() {
        this.game.num++
    }

    fun addText() {
        val a = this.network.getText()
            console.log( a )
            a.then { str: String ->
                console.log( str )
                this.game.message = this.game.message + str
            }
    }

    private fun incomingMessage( message: String ) {
        console.log( "received $message" )
        this.game.message += message
    }

}