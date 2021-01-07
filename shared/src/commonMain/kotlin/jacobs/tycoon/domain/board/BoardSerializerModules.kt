package jacobs.tycoon.domain.board

import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun boardSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic( Board::class ) {
            standardBoards()
        }
        polymorphic( StandardBoard::class ) {
            standardBoards()
        }
    }
}

private fun PolymorphicModuleBuilder < StandardBoard >.standardBoards() {
    subclass( LondonBoard::class, LondonBoard.serializer() )
}