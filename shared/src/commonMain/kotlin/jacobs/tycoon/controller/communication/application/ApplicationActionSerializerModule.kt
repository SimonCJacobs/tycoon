package jacobs.tycoon.controller.communication.application

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun applicationActionSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( ApplicationAction::class ) {
            AccessAdminModeRequest::class with AccessAdminModeRequest.serializer()
            UpdateCashHoldingsRequest::class with UpdateCashHoldingsRequest.serializer()
        }
    }
}