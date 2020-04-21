@file:JsModule( "mithril" )
@file:JsNonModule
package external.mithril

import org.w3c.dom.Element
import kotlin.js.Promise

@JsExport @ExperimentalJsExport
external fun m( selector: String?, attributes: Any?, vNode: VNode? ): VNode
external fun m( selector: String?, attributes: Any?, vNodes: Array < VNode > ): VNode
external fun m( selector: String?, attributes: Any?, text: String ): VNode
external fun m( selector: String?, attributes: Any?, number: Number ): VNode
external fun m( selector: String?, attributes: Any?, boolean: Boolean ): VNode
external fun m( selector: String ) : VNode
external fun m( component: Component ): VNode

external fun mount( element: Element, component: Component )

external fun redraw()

external fun render( element: Element, text: String )
external fun render( element: Element, vNode: VNode )

external fun request( options: SimpleRequestOptions ) : Promise < String >
external fun < T > request( options: ExtractionRequestOptions < T > ) : Promise < T >