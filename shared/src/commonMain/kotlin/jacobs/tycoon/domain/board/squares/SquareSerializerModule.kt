package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun squareSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic( Property::class ) {
            properties()
        }
        polymorphic( ActionSquare::class ) {
            actionSquares()
        }
        polymorphic( Square::class ) {
            properties()
            actionSquares()
            squares()
        }
    }
}

private fun PolymorphicModuleBuilder < Property >.properties() {
    subclass( NullProperty::class, NullProperty.serializer() )
    subclass( Street::class, Street.serializer() )
    subclass( Station::class, Station.serializer() )
    subclass( Utility::class, Utility.serializer() )
}

private fun PolymorphicModuleBuilder < ActionSquare >.actionSquares() {
    subclass( CardSquare::class, CardSquare.serializer() )
    subclass( FreeParkingSquare::class, FreeParkingSquare.serializer() )
    subclass( GoSquare::class, GoSquare.serializer() )
    subclass( GoToJailSquare::class, GoToJailSquare.serializer() )
    subclass( JailSquare::class, JailSquare.serializer() )
    subclass( JustVisitingJailSquare::class, JustVisitingJailSquare.serializer() )
    subclass( TaxSquare::class, TaxSquare.serializer() )
}

private fun PolymorphicModuleBuilder < Square >.squares() {
    subclass( NullSquare::class, NullSquare.serializer() )
}