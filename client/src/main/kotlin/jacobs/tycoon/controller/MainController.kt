package jacobs.tycoon.controller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.network.Network
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.ViewState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MainController ( kodein: Kodein ) {

    private val game: Game by kodein.instance()
    private val network: Network by kodein.instance()
    private val state: GameState by kodein.instance()
    private val clientState: ClientState by kodein.instance()

    init {
        this.network.registerMessageReceiver( this::incomingMessage )
    }

    fun addPlayer( name: String, playingPiece: PlayingPiece ) {
        this.game.addPlayer( name, playingPiece )
            .also {
                this.state.players.addPlayer( it )
            }
    }

    fun goToPlayingAreaView() {
        this.clientState.viewState = ViewState.PLAYING_AREA
    }

    fun getGameStage(): GameStage {
        return this.state.stage
    }

    @Suppress( "MemberVisibilityCanBePrivate" )
    fun incomingMessage( message: String ) {
        TODO()
    }

    fun getViewStage(): ViewState {
        return this.clientState.viewState
    }

}