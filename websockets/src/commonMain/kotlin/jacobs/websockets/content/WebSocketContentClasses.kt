package jacobs.websockets.content

fun getWebSocketContentClasses(): ContentClassCollection {
    return ContentClassCollection.build {
        BooleanContent::class serializedBy  BooleanContent.serializer()
        StringContent::class serializedBy StringContent.serializer()
    }
}