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
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule

fun gameActionSerializerModule(): SerializersModule {
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

private fun PolymorphicModuleBuilder < GameAction >.gameActions() {
    subclass( AuctionNotification::class, AuctionNotification.serializer() )
    subclass( CompleteSignUp::class, CompleteSignUp.serializer() )
    subclass( ConcludeAuction::class, ConcludeAuction.serializer() )
    subclass( CarryOutBankruptcyProceedings::class, CarryOutBankruptcyProceedings.serializer() )
    subclass( NewGame::class, NewGame.serializer() )
    subclass( SetBoard::class, SetBoard.serializer() )
    subclass( SetPieces::class, SetPieces.serializer() )
    subclass( UpdateCashHoldings::class, UpdateCashHoldings.serializer() )
}

private fun PolymorphicModuleBuilder < PositionalGameAction >.positionalGameActions() {
    subclass( AcceptFunds::class, AcceptFunds.serializer() )
    subclass( AttemptToPay::class, AttemptToPay.serializer() )
    subclass( AuctionBid::class, AuctionBid.serializer() )
    subclass( Build::class, Build.serializer() )
    subclass( MortgageProperty::class, MortgageProperty.serializer() )
    subclass( OfferTrade::class, OfferTrade.serializer() )
    subclass( PayFineOrTakeCard::class, PayFineOrTakeCard.serializer() )
    subclass( PayOffMortgage::class, PayOffMortgage.serializer() )
    subclass( PieceMoved::class, PieceMoved.serializer() )
    subclass( PlayGetOutOfJailFreeCard::class, PlayGetOutOfJailFreeCard.serializer() )
    subclass( PayJailFineVoluntarily::class, PayJailFineVoluntarily.serializer() )
    subclass( ReadCard::class, ReadCard.serializer() )
    subclass( RentCharge::class, RentCharge.serializer() )
    subclass( RespondToPropertyOffer::class, RespondToPropertyOffer.serializer() )
    subclass( RespondToTradeOffer::class, RespondToTradeOffer.serializer() )
    subclass( RollForMove::class, RollForMove.serializer() )
    subclass( RollForMoveFromJail::class, RollForMoveFromJail.serializer() )
    subclass( RollForOrderAction::class, RollForOrderAction.serializer() )
    subclass( SellBuildings::class, SellBuildings.serializer() )
    subclass( UseGetOutOfJailFreeCard::class, UseGetOutOfJailFreeCard.serializer() )
}

private fun PolymorphicModuleBuilder < GameActionWithResponse >.gameActionsWithResponse() {
    subclass( AddPlayer::class, AddPlayer.serializer() )
}
