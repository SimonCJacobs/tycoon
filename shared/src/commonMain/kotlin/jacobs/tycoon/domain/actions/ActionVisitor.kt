package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.actions.auction.AuctionBid
import jacobs.tycoon.domain.actions.auction.AuctionNotification
import jacobs.tycoon.domain.actions.auction.ConcludeAuction
import jacobs.tycoon.domain.actions.cards.PayFineOrTakeCard
import jacobs.tycoon.domain.actions.debt.CarryOutBankruptcyProceedings
import jacobs.tycoon.domain.actions.cards.PlayGetOutOfJailFreeCard
import jacobs.tycoon.domain.actions.cards.ReadCard
import jacobs.tycoon.domain.actions.debt.AcceptFunds
import jacobs.tycoon.domain.actions.debt.AttemptToPay
import jacobs.tycoon.domain.actions.gameadmin.AddPlayer
import jacobs.tycoon.domain.actions.gameadmin.CompleteSignUp
import jacobs.tycoon.domain.actions.gameadmin.NewGame
import jacobs.tycoon.domain.actions.gameadmin.SetBoard
import jacobs.tycoon.domain.actions.gameadmin.SetPieces
import jacobs.tycoon.domain.actions.jail.PayJailFineVoluntarily
import jacobs.tycoon.domain.actions.jail.RollForMoveFromJail
import jacobs.tycoon.domain.actions.jail.UseGetOutOfJailFreeCard
import jacobs.tycoon.domain.actions.moving.PieceMoved
import jacobs.tycoon.domain.actions.moving.RollForMove
import jacobs.tycoon.domain.actions.moving.RollForOrderAction
import jacobs.tycoon.domain.actions.property.Build
import jacobs.tycoon.domain.actions.property.DealWithMortgageOnTransfer
import jacobs.tycoon.domain.actions.property.MortgageProperty
import jacobs.tycoon.domain.actions.property.PayOffMortgage
import jacobs.tycoon.domain.actions.property.RentCharge
import jacobs.tycoon.domain.actions.property.RespondToPropertyOffer
import jacobs.tycoon.domain.actions.property.SellBuildings
import jacobs.tycoon.domain.actions.trading.OfferTrade
import jacobs.tycoon.domain.actions.trading.RespondToTradeOffer

interface ActionVisitor < T > {
    fun visit( acceptFunds: AcceptFunds ): T
    fun visit( addPlayer: AddPlayer ): T
    fun visit( attemptToPay: AttemptToPay ): T
    fun visit( auctionBid: AuctionBid ): T
    fun visit( auctionNotification: AuctionNotification ): T
    fun visit( build: Build ): T
    fun visit( completeSignUp: CompleteSignUp ): T
    fun visit( concludeAuction: ConcludeAuction ): T
    fun visit( carryOutBankruptcyProceedings: CarryOutBankruptcyProceedings ): T
    fun visit( dealWithMortgageOnTransfer: DealWithMortgageOnTransfer ): T
    fun visit( mortgageProperty: MortgageProperty ): T
    fun visit( newGame: NewGame ): T
    fun visit( offerTrade: OfferTrade ): T
    fun visit( payFineOrTakeCard: PayFineOrTakeCard ): T
    fun visit( payJailFineVoluntarily: PayJailFineVoluntarily ): T
    fun visit( payOffMortgage: PayOffMortgage ): T
    fun visit( pieceMoved: PieceMoved ): T
    fun visit( playGetOutOfJailFreeCard: PlayGetOutOfJailFreeCard ): T
    fun visit( readCard: ReadCard ): T
    fun visit( rentCharge: RentCharge ): T
    fun visit( respondToPropertyOffer: RespondToPropertyOffer ): T
    fun visit( respondToTradeOffer: RespondToTradeOffer): T
    fun visit( rollForMove: RollForMove ): T
    fun visit( rollForMoveFromJail: RollForMoveFromJail ): T
    fun visit( rollForOrder: RollForOrderAction ): T
    fun visit( sellBuildings: SellBuildings ): T
    fun visit( setBoard: SetBoard ): T
    fun visit( setPieces: SetPieces ): T
    fun visit( useGetOutOfJailFreeCard: UseGetOutOfJailFreeCard ): T
}