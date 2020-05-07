package jacobs.mithril

import org.js.mithril.LifecycleMethod
import org.w3c.dom.DragEvent
import org.w3c.dom.GlobalEventHandlers
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import org.w3c.dom.events.KeyboardEvent

class EventHandlers {

    // Mithril lifecycle methods

    var onupdate: LifecycleMethod? = null

    // Combined methods

    var onInputExt: EventHandler? = null
        set( handler ) {
            this.onclick = handler
            this.oninput = handler
            this.onkeyup = handler
            field = handler
        }

    // W3C events

    var onclick: MouseEventHandler? = null
    var ondrag: DragEventHandler? = null
    var ondragend: DragEventHandler? = null
    var ondragenter: DragEventHandler? = null
    var ondragexit: DragEventHandler? = null
    var ondragleave: DragEventHandler? = null
    var ondragover: DragEventHandler? = null
    var ondragstart: DragEventHandler? = null
    var ondrop: DragEventHandler? = null
    var oninput: InputEventHandler? = null
    var onkeyup: KeyboardEventHandler? = null

}

