package jacobs.tycoon.controller.communication.application

import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun applicationActionSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic( ApplicationAction::class ) {
            subclass( AccessAdminModeRequest::class, AccessAdminModeRequest.serializer() )
            subclass( UpdateCashHoldingsRequest::class, UpdateCashHoldingsRequest.serializer() )
        }
    }
}