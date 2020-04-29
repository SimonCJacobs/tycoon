package jacobs.websockets

import jacobs.websockets.content.ContentClassCollection

abstract class WebSockets(
    contentClassesParameter: ContentClassCollection?
) {
    protected val contentClasses: ContentClassCollection = contentClassesParameter ?: ContentClassCollection()
}

