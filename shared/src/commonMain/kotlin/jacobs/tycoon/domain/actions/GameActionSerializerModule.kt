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
import jacobs.tycoon.domain.actions.gameadmin.UpdateCashHoldings
import jacobs.tycoon.domain.actions.jail.PayJailFineVoluntarily
import jacobs.tycoon.domain.actions.jail.RollForMoveFromJail
import jacobs.tycoon.domain.actions.jail.UseGetOutOfJailFreeCard
import jacobs.tycoon.domain.actions.moving.PieceMoved
import jacobs.tycoon.domain.actions.moving.RollForMove
import jacobs.tycoon.domain.actions.moving.RollForOrderAction
import jacobs.tycoon.domain.actions.property.Build
import jacobs.tycoon.domain.actions.property.MortgageProperty
import jacobs.tycoon.domain.actions.property.PayOffMortgage
import jacobs.tycoon.domain.actions.property.RentCharge
import jacobs.tycoon.domain.actions.property.RespondToPropertyOffer
import jacobs.tycoon.domain.actions.property.SellBuildings
import jacobs.tycoon.domain.actions.trading.OfferTrade
import jacobs.tycoon.domain.actions.trading.RespondToTradeOffer
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun gameActionSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( GameAction::class ) {
            AcceptFunds::class with AcceptFunds.serializer()
            AddPlayer::class with AddPlayer.serializer()
            AttemptToPay::class with AttemptToPay.serializer()
            AuctionBid::class with AuctionBid.serializer()
            AuctionNotification::class with AuctionNotification.serializer()
            Build::class with Build.serializer()
            CompleteSignUp::class with CompleteSignUp.serializer()
            ConcludeAuction::class with ConcludeAuction.serializer()
            CarryOutBankruptcyProceedings::class with CarryOutBankruptcyProceedings.serializer()
            MortgageProperty::class with MortgageProperty.serializer()
            NewGame::class with NewGame.serializer()
            OfferTrade::class with OfferTrade.serializer()
            PayFineOrTakeCard::class with PayFineOrTakeCard.serializer()
            PayJailFineVoluntarily::class with PayJailFineVoluntarily.serializer()
            PayOffMortgage::class with PayOffMortgage.serializer()
            PieceMoved::class with PieceMoved.serializer()
            PlayGetOutOfJailFreeCard::class with PlayGetOutOfJailFreeCard.serializer()
            ReadCard::class with ReadCard.serializer()
            RentCharge::class with RentCharge.serializer()
            RespondToPropertyOffer::class with RespondToPropertyOffer.serializer()
            RespondToTradeOffer::class with RespondToTradeOffer.serializer()
            RollForMove::class with RollForMove.serializer()
            RollForMoveFromJail::class with RollForMoveFromJail.serializer()
            RollForOrderAction::class with RollForOrderAction.serializer()
            SellBuildings::class with SellBuildings.serializer()
            SetBoard::class with SetBoard.serializer()
            SetPieces::class with SetPieces.serializer()
            UpdateCashHoldings::class with UpdateCashHoldings.serializer()
            UseGetOutOfJailFreeCard::class with UseGetOutOfJailFreeCard.serializer()
        }
    }
}