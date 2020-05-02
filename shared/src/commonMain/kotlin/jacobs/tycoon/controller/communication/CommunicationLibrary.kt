package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.board.boardSerializerModule
import jacobs.tycoon.domain.board.squareSerializerModule
import jacobs.tycoon.domain.pieces.pieceSetSerializerModule
import jacobs.tycoon.state.StateUpdate
import jacobs.websockets.content.SerializationLibrary

class CommunicationLibrary {

    val serializationLibrary = SerializationLibrary.build {

        AddPlayerRequest::class serializedBy AddPlayerRequest.serializer()
        SimpleRequestWrapper::class serializedBy SimpleRequestWrapper.serializer()
        StateUpdate::class serializedBy StateUpdate.serializer()

        serialModule( boardSerializerModule() )
        serialModule( pieceSetSerializerModule() )
        serialModule( squareSerializerModule() )

    }

}