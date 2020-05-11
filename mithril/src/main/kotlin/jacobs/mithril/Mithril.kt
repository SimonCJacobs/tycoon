package jacobs.mithril

import jacobs.jsutilities.toJsObject
import org.js.mithril.Component
import org.js.mithril.mount as mountJs
import org.js.mithril.redraw as redrawJs
import org.js.mithril.route as routeJs
import org.w3c.dom.Element

@ExperimentalJsExport
class Mithril {

    val hyperScriptFactory = HyperScriptFactory()

    fun mount( element: Element, component: Component ) {
        return mountJs( element, component )
    }

    fun redraw() {
        redrawJs()
    }

    fun route( element: Element, defaultRoute: String, routeMap: Map < String, Component > ) {
        return routeJs( element, defaultRoute, routeMap.toJsObject() )
    }

}