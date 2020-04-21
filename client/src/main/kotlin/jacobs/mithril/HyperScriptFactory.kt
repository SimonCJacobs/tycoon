package jacobs.mithril

import external.mithril.Component
import external.mithril.VNode
import external.mithril.m
import jacobs.jsutilities.jsObject

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

class HyperScriptBuilder(
    private val tag: Tag?,
    closure: Details.() -> Unit
) {

    private var attributes: Any? = null
    private var child: VNode? = null
    private var children: Collection < VNode >? = null
    private var stringContents: String? = null

    init {
        Details( this ).closure()
    }

    @ExperimentalJsExport
    fun build() : VNode {
        if ( null != this.child )
            return m( this.tag.toString(), this.attributes, this.child )
        this.children.also {
            if ( null != it )
                return m( this.tag.toString(), this.attributes, it.toTypedArray() )
        }
        this.stringContents.also {
            if ( null != it )
                return m( this.tag.toString(), this.attributes, it )
        }
        this.tag.also {
            if ( null != it )
                return m( it.toString() )
        }
        throw Error( "Unknown hyperscript format: all arguments null (*should* never get here!)" )
    }

    class Details(
        private val builder: HyperScriptBuilder
    ) {

        fun attributes( attributeClosure: dynamic.() -> Unit ) {
            this.builder.attributes = jsObject( attributeClosure )
        }

        fun child( child: VNode ) {
            this.builder.child = child
        }

        fun children( children: Collection < VNode > ) {
            this.builder.children = children
        }

        fun contents( stringContents: String ) {
            this.builder.stringContents = stringContents
        }

    }

}
