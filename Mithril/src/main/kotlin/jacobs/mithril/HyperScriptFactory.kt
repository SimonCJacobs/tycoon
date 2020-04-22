package jacobs.mithril

import org.js.mithril.Component
import org.js.mithril.VNode
import org.js.mithril.m

@ExperimentalJsExport
val m = HyperScriptFactory()

@ExperimentalJsExport
class HyperScriptFactory {

    operator fun invoke( tag: Tag?, hyperScriptClosure: HyperScriptBuilder.Details.() -> Unit ) : VNode {
        return HyperScriptBuilder( tag, hyperScriptClosure )
            .build()
    }

    operator fun invoke( tag: Tag ) : VNode {
        return this( tag ) {}
    }

    operator fun invoke( component: Component ) : VNode {
        return m( component )
    }

    fun map( components: Collection < Component > ) : Collection < VNode > {
        return components.map { m ( it ) }
    }

}