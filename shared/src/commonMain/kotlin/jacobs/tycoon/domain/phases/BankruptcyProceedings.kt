package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player

class BankruptcyProceedings(
    override val playerWithTurn: Player,
    val bankruptPlayer: Player,
    private val creditor: Player? = null
) : TurnBasedPhase {

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun areAuctionsRequired(): Boolean {
        return null == creditor && bankruptPlayer.ownsAnyProperty()
    }

    fun carryOutProceedings( game: Game ) {
        game.players.eliminatePlayerFromGame( bankruptPlayer )
        if ( creditor != null )
            this.transferPropertiesToCreditor( creditor )
    }

    fun forEachPropertyOfBankruptPlayer( callback: ( Property ) -> Unit ) {
        this.bankruptPlayer.forEachPropertyOwned( callback )
    }

    private fun transferPropertiesToCreditor( knownCreditor: Player ) {
        bankruptPlayer.getAllAssets()
            .transfer( bankruptPlayer, knownCreditor )
    }

}