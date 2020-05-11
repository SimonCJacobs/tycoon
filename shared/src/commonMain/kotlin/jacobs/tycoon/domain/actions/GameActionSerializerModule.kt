package jacobs.tycoon.domain.actions

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun gameActionSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( GameAction::class ) {
            AddPlayer::class with AddPlayer.serializer()
            CompleteSignUp::class with CompleteSignUp.serializer()
            NewGame::class with NewGame.serializer()
            PieceMoved::class with PieceMoved.serializer()
            ReadCard::class with ReadCard.serializer()
            RentCharge::class with RentCharge.serializer()
            RespondToPropertyOffer::class with RespondToPropertyOffer.serializer()
            RollForMove::class with RollForMove.serializer()
            RollForOrderAction::class with RollForOrderAction.serializer()
            SetBoard::class with SetBoard.serializer()
            SetPieces::class with SetPieces.serializer()
        }
    }
}