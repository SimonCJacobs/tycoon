package jacobs.mithril

import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent

typealias DragEventHandler = ( DragEvent ) -> Unit
typealias EventHandler = ( Event ) -> Unit
typealias KeyboardEventHandler = ( KeyboardEvent ) -> Unit
typealias InputEventHandler = ( InputEvent ) -> Unit
typealias MouseEventHandler = ( MouseEvent ) -> Unit