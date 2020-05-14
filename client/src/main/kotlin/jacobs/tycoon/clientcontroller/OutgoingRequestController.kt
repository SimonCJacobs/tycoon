package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.ActionRequest
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.domain.actions.gameadmin.AddPlayer
import jacobs.tycoon.domain.actions.gameadmin.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.auction.AuctionBid
import jacobs.tycoon.domain.actions.moving.PieceMoved
import jacobs.tycoon.domain.actions.cards.ReadCard
import jacobs.tycoon.domain.actions.property.RentCharge
import jacobs.tycoon.domain.actions.property.RespondToPropertyOffer
import jacobs.tycoon.domain.actions.moving.RollForMove
import jacobs.tycoon.domain.actions.moving.RollForOrderAction
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.services.Network
import jacobs.websockets.content.BooleanContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class OutgoingRequestController( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val network by kodein.instance <Network> ()

    suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): Boolean {
        return this.sendActionRequest(AddPlayer(playerName, playingPiece))
    }

    suspend fun chargeRent(): Boolean {
        return this.sendActionRequest(RentCharge())
    }

    suspend fun completeGameSignUp(): Boolean {
        return this.sendActionRequest(CompleteSignUp())
    }

    suspend fun makeBid( bidAmount: CurrencyAmount ): Boolean {
        return this.sendActionRequest( AuctionBid( bidAmount ) )
    }

    suspend fun pieceMoved(): Boolean {
        return this.sendActionRequest(PieceMoved())
    }

    suspend fun readCard(): Boolean {
        return this.sendActionRequest(ReadCard())
    }

    suspend fun respondToPropertyOffer( isBuying: Boolean ): Boolean {
        return this.sendActionRequest(RespondToPropertyOffer(isBuying))
    }

    suspend fun rollTheDiceForMove(): Boolean {
        return this.sendActionRequest(RollForMove())
    }

    suspend fun rollTheDiceForOrder(): Boolean {
        return this.sendActionRequest(RollForOrderAction())
    }

    private suspend fun sendActionRequest( action: GameAction ): Boolean {
        return this.sendYesNoRequest( ActionRequest( action ) )
    }

    private suspend fun sendYesNoRequest( request: Request ): Boolean {
        this.clientState.isWaitingForServer = true
        return this.sendRequestOnNetwork( request )
    }

    private suspend fun sendRequestOnNetwork( request: Request ): Boolean {
        return this.network.sendRequest( request )
            .let { response -> ( response as BooleanContent ).boolean }
    }

}