@file:JsModule( "mithril" )
@file:JsNonModule
package org.js.mithril

import org.w3c.dom.Element
import kotlin.js.Promise

@JsExport @ExperimentalJsExport
internal external fun m( selector: String?, attributes: Any?, vNode: VNode? ): VNode
internal external fun m( selector: String?, attributes: Any?, vNodes: Array < out VNode > ): VNode
internal external fun m( selector: String?, attributes: Any?, text: String ): VNode
internal external fun m( selector: String?, attributes: Any?, number: Number ): VNode
internal external fun m( selector: String?, attributes: Any?, boolean: Boolean ): VNode
internal external fun m( component: Component ): VNode

internal external fun mount( element: Element, component: Component )

internal external fun redraw()

internal external fun render( element: Element, text: String )
internal external fun render( element: Element, vNode: VNode )

internal external fun request( options: SimpleRequestOptions ) : Promise < String >
internal external fun < T > request( options: ExtractionRequestOptions < T > ) : Promise < T >