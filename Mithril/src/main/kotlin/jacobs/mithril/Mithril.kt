package jacobs.mithril

import org.js.mithril.Component
import org.js.mithril.mount as mountJs
import org.w3c.dom.Element

@ExperimentalJsExport
class Mithril {

    val hyperScriptFactory = HyperScriptFactory()

    fun mount( element: Element, component: Component ) {
        return mountJs( element, component )
    }

}