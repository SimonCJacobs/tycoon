package jacobs.mithril

import jacobs.jsutilities.jsObject
import org.js.mithril.VNode

class HyperScriptBuilder(
    private val tag: Tag?,
    closure: Details.() -> Unit
) {

    private var attributes: Any = jsObject()
    private var child: VNode? = null
    private var children: Array < out VNode>? = null
    private var stringContents: String? = null

    init {
        Details( this ).closure()
    }

    @ExperimentalJsExport
    fun build() : VNode {
        if ( null != this.child )
            return org.js.mithril.m(this.tag.toString(), this.attributes, this.child)
        this.children.also {
            if ( null != it )
                return org.js.mithril.m(this.tag.toString(), this.attributes, it)
        }
        this.stringContents.also {
            if ( null != it )
                return org.js.mithril.m(this.tag.toString(), this.attributes, it)
        }
        this.tag.also {
            if ( null != it )
                return org.js.mithril.m(it.toString(), this.attributes, "")
        }
        throw Error( "Unknown hyperscript format: all arguments null (*should* never get here!)" )
    }

    class Details(
        private val builder: HyperScriptBuilder
    ) {

        fun attributes( attributeClosure: dynamic.() -> Unit ) {
            this.builder.attributes = jsObject( attributeClosure )
        }

        fun child( child: VNode) {
            this.builder.child = child
        }

        fun children( children: Collection <VNode> ) {
            this.builder.children = children.toTypedArray()
        }

        fun children( vararg children: VNode) {
            this.builder.children = children
        }

        fun content( stringContents: String ) {
            this.builder.stringContents = stringContents
        }

        fun eventHandlers( eventHandlerClosure: EventHandlers.() -> Unit ) {
            EventHandlers()
                .apply {
                    eventHandlerClosure()
                    mergeIntoJsObject( builder.attributes )
                }
        }

    }

}