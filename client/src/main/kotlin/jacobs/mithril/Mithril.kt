package jacobs.mithril

import external.mithril.Component
import external.mithril.mount as mountJs
import org.w3c.dom.Element

class Mithril {

    val hyperScriptFactory = HyperScriptFactory()

    fun mount( element: Element, component: Component ) {
        return mountJs( element, component )
    }

}