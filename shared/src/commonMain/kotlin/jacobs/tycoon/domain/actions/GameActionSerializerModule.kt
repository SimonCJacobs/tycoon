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
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun gameActionSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( GameAction::class ) {
            gameActions()
            positionalGameActions()
            gameActionsWithResponse()
        }
        polymorphic( PositionalGameAction::class ) {
            positionalGameActions()
        }
        polymorphic( GameActionWithResponse::class ) {
            gameActionsWithResponse()
        }
    }
}

private fun PolymorphicModuleBuilder < in GameAction >.gameActions() {
    AuctionNotification::class with AuctionNotification.serializer()
    CompleteSignUp::class with CompleteSignUp.serializer()
    ConcludeAuction::class with ConcludeAuction.serializer()
    CarryOutBankruptcyProceedings::class with CarryOutBankruptcyProceedings.serializer()
    NewGame::class with NewGame.serializer()
    SetBoard::class with SetBoard.serializer()
    SetPieces::class with SetPieces.serializer()
    UpdateCashHoldings::class with UpdateCashHoldings.serializer()
}

private fun PolymorphicModuleBuilder < in PositionalGameAction >.positionalGameActions() {
    AcceptFunds::class with AcceptFunds.serializer()
    AttemptToPay::class with AttemptToPay.serializer()
    AuctionBid::class with AuctionBid.serializer()
    Build::class with Build.serializer()
    MortgageProperty::class with MortgageProperty.serializer()
    OfferTrade::class with OfferTrade.serializer()
    PayFineOrTakeCard::class with PayFineOrTakeCard.serializer()
    PayOffMortgage::class with PayOffMortgage.serializer()
    PieceMoved::class with PieceMoved.serializer()
    PlayGetOutOfJailFreeCard::class with PlayGetOutOfJailFreeCard.serializer()
    PayJailFineVoluntarily::class with PayJailFineVoluntarily.serializer()
    ReadCard::class with ReadCard.serializer()
    RentCharge::class with RentCharge.serializer()
    RespondToPropertyOffer::class with RespondToPropertyOffer.serializer()
    RespondToTradeOffer::class with RespondToTradeOffer.serializer()
    RollForMove::class with RollForMove.serializer()
    RollForMoveFromJail::class with RollForMoveFromJail.serializer()
    RollForOrderAction::class with RollForOrderAction.serializer()
    SellBuildings::class with SellBuildings.serializer()
    UseGetOutOfJailFreeCard::class with UseGetOutOfJailFreeCard.serializer()
}

private fun PolymorphicModuleBuilder < in GameActionWithResponse >.gameActionsWithResponse() {
    AddPlayer::class with AddPlayer.serializer()
}
