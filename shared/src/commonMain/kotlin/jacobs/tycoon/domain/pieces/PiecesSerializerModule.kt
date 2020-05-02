package jacobs.tycoon.domain.pieces

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun pieceSetSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( PieceSet::class ) {
            ClassicPieces::class with ClassicPieces.serializer()
        }
    }
}