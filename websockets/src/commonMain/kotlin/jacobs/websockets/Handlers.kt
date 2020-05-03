package jacobs.websockets

import jacobs.websockets.content.MessageContent

typealias NewConnectionHandler = suspend ( SocketId ) -> Unit
typealias NotificationHandler = suspend ( MessageContent ) -> Unit
typealias RequestHandler = suspend ( MessageContent ) -> MessageContent
typealias RequestHandlerWithIndex = suspend ( MessageContent, SocketId ) -> MessageContent