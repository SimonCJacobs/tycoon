package jacobs.tycoon.domain.actions.auction

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.services.auction.AuctionStatus
import kotlinx.serialization.Serializable

@Serializable
class ConcludeAuction : GameAction() {

    var status: AuctionStatus = AuctionStatus.NULL

    override fun < T > accept(visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        this.status = gameController.concludeAuction()
        executedSuccessfully()
    }

}
