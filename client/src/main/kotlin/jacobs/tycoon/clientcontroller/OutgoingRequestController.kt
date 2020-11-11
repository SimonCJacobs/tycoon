package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.GameActionRequest
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.application.AccessAdminModeRequest
import jacobs.tycoon.controller.communication.application.ApplicationAction
import jacobs.tycoon.controller.communication.application.ApplicationRequest
import jacobs.tycoon.controller.communication.application.UpdateCashHoldingsRequest
import jacobs.tycoon.domain.actions.gameadmin.AddPlayer
import jacobs.tycoon.domain.actions.gameadmin.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
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
import jacobs.tycoon.services.Network
import jacobs.websockets.content.BooleanContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class OutgoingRequestController( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val network by kodein.instance <Network> ()

    suspend fun acceptFunds(): Boolean {
        return this.sendActionRequest( AcceptFunds() )
    }

    suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): Boolean {
        return this.sendActionRequest( AddPlayer(playerName, playingPiece) )
    }

    suspend fun attemptToPay(): Boolean {
        return this.sendActionRequest( AttemptToPay() )
    }

    suspend fun buildHouses( streets: List < Street >, houseToBuild: List < Int > ): Boolean {
        return this.sendActionRequest( Build( streets, houseToBuild ) )
    }

    suspend fun carryOutBankruptcy(): Boolean {
        return this.sendActionRequest( CarryOutBankruptcyProceedings() )
    }

    suspend fun chargeRent( property: Property ): Boolean {
        return this.sendActionRequest( RentCharge( property ) )
    }

    suspend fun completeGameSignUp(): Boolean {
        return this.sendActionRequest( CompleteSignUp() )
    }

    suspend fun makeAdminEntryRequest( username: String ): Boolean {
        return this.sendApplicationActionRequest( AccessAdminModeRequest( username ) )
    }

    suspend fun makeBid( bidAmount: CurrencyAmount ): Boolean {
        return this.sendActionRequest( AuctionBid( bidAmount ) )
    }

    suspend fun offerTrade( offer: TradeOffer ): Boolean {
        return this.sendActionRequest( OfferTrade( offer ) )
    }

    suspend fun payFineOrTakeChance( decision: PayFineOrTakeCardDecision ): Boolean {
        return this.sendActionRequest( PayFineOrTakeCard( decision ) )
    }

    suspend fun payJailFine(): Boolean {
        return this.sendActionRequest( PayJailFineVoluntarily() )
    }

    suspend fun payOffMortgages( properties: Collection < Property > ): Boolean {
        return this.sendActionRequest( PayOffMortgage( properties ) )
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

    suspend fun respondToTradeOffer( response: Boolean ): Boolean {
        return this.sendActionRequest( RespondToTradeOffer( response ) )
    }

    suspend fun rollTheDiceFromJail(): Boolean {
        return this.sendActionRequest( RollForMoveFromJail() )
    }

    suspend fun rollTheDiceForMove(): Boolean {
        return this.sendActionRequest( RollForMove() )
    }

    suspend fun rollTheDiceForOrder(): Boolean {
        return this.sendActionRequest( RollForOrderAction() )
    }

    suspend fun sellHouses( streets: List < Street >, housesToSell: List < Int > ): Boolean {
        return this.sendActionRequest( SellBuildings( streets, housesToSell ) )
    }

    suspend fun takeOutMortgages( properties: Collection < Property > ): Boolean {
        return this.sendActionRequest( MortgageProperty( properties ) )
    }

    suspend fun updateCashHoldingsRequest( player: Player, newCashHoldings: CurrencyAmount ): Boolean {
        return this.sendApplicationActionRequest( UpdateCashHoldingsRequest( player, newCashHoldings ) )
    }

    suspend fun useGetOutOfJailFreeCard(): Boolean {
        return this.sendActionRequest( UseGetOutOfJailFreeCard() )
    }

    private suspend fun sendActionRequest( action: GameAction ): Boolean {
        return this.sendYesNoRequest( GameActionRequest( action ) )
    }

    private suspend fun sendApplicationActionRequest( action: ApplicationAction ): Boolean {
        return this.sendYesNoRequest( ApplicationRequest( action ) )
    }

    private suspend fun sendYesNoRequest( request: Request ): Boolean {
        this.clientState.isWaitingForServer = true
        val response = this.sendRequestOnNetwork( request )
        if ( response == false ) {
            console.log( "Request unsuccessful $request" )
            this.clientState.isWaitingForServer = false
        }
        return response
    }

    private suspend fun sendRequestOnNetwork( request: Request ): Boolean {
        return this.network.sendRequest( request )
            .let { response -> ( response as BooleanContent ).boolean }
    }

}