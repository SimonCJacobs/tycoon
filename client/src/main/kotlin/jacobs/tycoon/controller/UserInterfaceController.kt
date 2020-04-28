package jacobs.tycoon.controller

import jacobs.mithril.Mithril
import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.network.Network
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pages.EntryPageState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class UserInterfaceController( kodein: Kodein ) {

    private val game by kodein.instance < Game > ()
    private val mainController by kodein.instance < MainController > ()
    private val network by kodein.instance < Network > ()
    private val state by kodein.instance < GameState > ()

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.game.getAvailablePieces(
            this.state.pieceSet,
            this.state.players
        )
    }

    fun getRandomAvailablePiece(): PlayingPiece {
        return this.getAvailablePieces()
            .random()
    }

    fun onEntryPageButtonClick( entryPageState: EntryPageState ) {
        console.log( "Button click" )
        this.mainController.addPlayer( entryPageState.playerName, entryPageState.selectedPiece )
        this.mainController.goToPlayingAreaView()
    }

    fun triggerNetworkRequest( entryPageState: EntryPageState ) {
        this.network.sendStringRequest( entryPageState.playerName ) {
            entryPageState.testText = it
            Mithril().redraw()
        }
    }

}