package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.BinaryGameActionRequest
import jacobs.tycoon.controller.communication.GameActionRequestWithResponse
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.application.AccessAdminModeRequest
import jacobs.tycoon.controller.communication.application.ApplicationAction
import jacobs.tycoon.controller.communication.application.ApplicationRequest
import jacobs.tycoon.controller.communication.application.UpdateCashHoldingsRequest
import jacobs.tycoon.domain.actions.gameadmin.AddPlayer
import jacobs.tycoon.domain.actions.gameadmin.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.GameActionWithResponse
import jacobs.tycoon.domain.actions.auction.AuctionBid
import jacobs.tycoon.domain.actions.cards.PayFineOrTakeCard
import jacobs.tycoon.domain.actions.cards.PayFineOrTakeCardDecision
import jacobs.tycoon.domain.actions.moving.PieceMoved
import jacobs.tycoon.domain.actions.cards.ReadCard
import jacobs.tycoon.domain.actions.debt.AcceptFunds
import jacobs.tycoon.domain.actions.debt.AttemptToPay
import jacobs.tycoon.domain.actions.debt.CarryOutBankruptcyProceedings
import jacobs.tycoon.domain.actions.jail.PayJailFineVoluntarily
import jacobs.tycoon.domain.actions.jail.RollForMoveFromJail
import jacobs.tycoon.domain.actions.jail.UseGetOutOfJailFreeCard
import jacobs.tycoon.domain.actions.property.RentCharge
import jacobs.tycoon.domain.actions.property.RespondToPropertyOffer
import jacobs.tycoon.domain.actions.moving.RollForMove
import jacobs.tycoon.domain.actions.moving.RollForOrderAction
import jacobs.tycoon.domain.actions.property.Build
import jacobs.tycoon.domain.actions.property.MortgageProperty
import jacobs.tycoon.domain.actions.property.PayOffMortgage
import jacobs.tycoon.domain.actions.property.SellBuildings
import jacobs.tycoon.domain.actions.trading.OfferTrade
import jacobs.tycoon.domain.actions.trading.RespondToTradeOffer
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.services.Network
import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class OutgoingRequestController( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val network by kodein.instance <Network> ()

    suspend fun acceptFunds(): Boolean {
        return this.sendActionRequest( AcceptFunds( seatingPosition() ) )
    }

    suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): SeatingPosition {
        return this.sendActionRequestWithResponse( AddPlayer( playerName, playingPiece ) )
    }

    suspend fun attemptToPay(): Boolean {
        return this.sendActionRequest( AttemptToPay( seatingPosition() ) )
    }

    suspend fun buildHouses( streets: List < Street >, houseToBuild: List < Int > ): Boolean {
        return this.sendActionRequest( Build( streets, houseToBuild, seatingPosition() ) )
    }

    suspend fun carryOutBankruptcy(): Boolean {
        return this.sendActionRequest( CarryOutBankruptcyProceedings() )
    }

    suspend fun chargeRent( property: Property ): Boolean {
        return this.sendActionRequest( RentCharge( property, seatingPosition() ) )
    }

    suspend fun completeGameSignUp(): Boolean {
        return this.sendActionRequest( CompleteSignUp() )
    }

    suspend fun makeAdminEntryRequest( username: String ): Boolean {
        return this.sendApplicationActionRequest( AccessAdminModeRequest( username ) )
    }

    suspend fun makeBid( bidAmount: CurrencyAmount ): Boolean {
        return this.sendActionRequest( AuctionBid( bidAmount, seatingPosition() ) )
    }

    suspend fun offerTrade( offer: TradeOffer ): Boolean {
        return this.sendActionRequest( OfferTrade( offer, seatingPosition() ) )
    }

    suspend fun payFineOrTakeChance( decision: PayFineOrTakeCardDecision ): Boolean {
        return this.sendActionRequest( PayFineOrTakeCard( decision, seatingPosition() ) )
    }

    suspend fun payJailFine(): Boolean {
        return this.sendActionRequest( PayJailFineVoluntarily( seatingPosition() ) )
    }

    suspend fun payOffMortgages( properties: Collection < Property > ): Boolean {
        return this.sendActionRequest( PayOffMortgage( properties, seatingPosition() ) )
    }

    suspend fun pieceMoved(): Boolean {
        return this.sendActionRequest( PieceMoved( seatingPosition() ) )
    }

    suspend fun readCard(): Boolean {
        return this.sendActionRequest( ReadCard( seatingPosition() ) )
    }

    suspend fun respondToPropertyOffer( isBuying: Boolean ): Boolean {
        return this.sendActionRequest( RespondToPropertyOffer( isBuying, seatingPosition() ) )
    }

    suspend fun respondToTradeOffer( response: Boolean ): Boolean {
        return this.sendActionRequest( RespondToTradeOffer( response, seatingPosition() ) )
    }

    suspend fun rollTheDiceFromJail(): Boolean {
        return this.sendActionRequest( RollForMoveFromJail( seatingPosition() ) )
    }

    suspend fun rollTheDiceForMove(): Boolean {
        return this.sendActionRequest( RollForMove( seatingPosition() ) )
    }

    suspend fun rollTheDiceForOrder(): Boolean {
        return this.sendActionRequest( RollForOrderAction( seatingPosition() ) )
    }

    suspend fun sellHouses( streets: List < Street >, housesToSell: List < Int > ): Boolean {
        return this.sendActionRequest( SellBuildings( streets, housesToSell, seatingPosition() ) )
    }

    suspend fun takeOutMortgages( properties: Collection < Property > ): Boolean {
        return this.sendActionRequest( MortgageProperty( properties, seatingPosition() ) )
    }

    suspend fun updateCashHoldingsRequest( player: Player, newCashHoldings: CurrencyAmount ): Boolean {
        return this.sendApplicationActionRequest( UpdateCashHoldingsRequest( player, newCashHoldings ) )
    }

    suspend fun useGetOutOfJailFreeCard(): Boolean {
        return this.sendActionRequest( UseGetOutOfJailFreeCard( seatingPosition() ) )
    }

    private fun seatingPosition(): SeatingPosition {
        return clientState.seatingPosition!!
    }

    private suspend fun sendActionRequest( action: GameAction ): Boolean {
        return this.sendYesNoRequest( BinaryGameActionRequest( action ) )
    }

    private suspend fun < TResponse : MessageContent > sendActionRequestWithResponse(
        action: GameActionWithResponse
    ): TResponse {
        return this.sendRequestOnNetwork( GameActionRequestWithResponse( action ) )
    }

    private suspend fun sendApplicationActionRequest( action: ApplicationAction ): Boolean {
        return this.sendYesNoRequest( ApplicationRequest( action ) )
    }

    private suspend fun sendYesNoRequest( request: Request ): Boolean {
        val response = this.sendRequestOnNetwork < BooleanContent > ( request ).boolean
        if ( response == false ) this.dealWithUnsuccessfulRequest( request )
        return response
    }

    @Suppress( "UNCHECKED_CAST" )
    private suspend fun < TResponse : MessageContent > sendRequestOnNetwork( request: Request ): TResponse {
        this.clientState.isWaitingForServer = true
        return this.network.sendRequest( request )
            .let { response -> ( response as TResponse ) }
    }

    private fun dealWithUnsuccessfulRequest( request: Request ) {
        console.log( "Request unsuccessful $request" )
        this.clientState.isWaitingForServer = false
    }

}