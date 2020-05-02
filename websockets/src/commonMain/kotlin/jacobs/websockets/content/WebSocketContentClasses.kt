package jacobs.websockets.content

fun getWebSocketContentClasses(): SerializationLibrary {
    return SerializationLibrary.build {
        BooleanContent::class serializedBy  BooleanContent.serializer()
        StringContent::class serializedBy StringContent.serializer()
    }
}