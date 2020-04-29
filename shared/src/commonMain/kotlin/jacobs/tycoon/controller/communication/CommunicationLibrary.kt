package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.pieces.PlayingPieceList
import jacobs.websockets.content.ContentClassCollection

class CommunicationLibrary {

    val contentClasses = ContentClassCollection.build {
        AddPlayerRequest::class serializedBy AddPlayerRequest.serializer()
        PlayingPieceList::class serializedBy PlayingPieceList.serializer()
        SimpleRequestWrapper::class serializedBy SimpleRequestWrapper.serializer()
    }

}