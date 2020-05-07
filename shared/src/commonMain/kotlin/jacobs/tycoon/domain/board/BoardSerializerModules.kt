package jacobs.tycoon.domain.board

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun boardSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( Board::class, StandardBoard::class ) {
            LondonBoard::class with LondonBoard.serializer()
        }
    }
}