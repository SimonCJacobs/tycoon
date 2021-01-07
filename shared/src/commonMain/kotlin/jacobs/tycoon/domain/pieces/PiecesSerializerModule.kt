package jacobs.tycoon.domain.pieces

import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun pieceSetSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic( PieceSet::class ) {
            subclass( ClassicPieces::class, ClassicPieces.serializer() )
        }
    }
}