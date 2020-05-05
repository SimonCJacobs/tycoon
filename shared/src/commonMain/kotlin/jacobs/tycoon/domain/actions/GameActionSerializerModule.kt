package jacobs.tycoon.domain.actions

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun gameActionSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( GameAction::class ) {
            AddPlayer::class with AddPlayer.serializer()
            CompleteSignUp::class with CompleteSignUp.serializer()
            NewGame::class with NewGame.serializer()
            RollForOrder::class with RollForOrder.serializer()
            SetBoard::class with SetBoard.serializer()
            SetPieces::class with SetPieces.serializer()
        }
    }
}