package jacobs.websockets.content

interface Messageable {
    fun toMessageContent(): MessageContent
}