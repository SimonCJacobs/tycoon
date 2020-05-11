package jacobs.mithril

import jacobs.jsutilities.absorb
import jacobs.jsutilities.jsObject
import org.js.mithril.VNode
import org.js.mithril.m

class HyperScriptBuilder(
    private val tag: Tag?,
    closure: Details.() -> Unit
) {

    private var attributes: MutableList < Any > = mutableListOf()
    private var child: VNode? = null
    private var children: Array < out VNode>? = null
    private var stringContents: String? = null

    init {
        Details( this ).closure()
    }

    @Suppress("UnsafeCastFromDynamic")
    private fun getAttributes(): dynamic {
        val combinedAttributes = jsObject()
        attributes.forEach { absorb( combinedAttributes, it ) }
        return combinedAttributes
    }

    @ExperimentalJsExport
    fun build() : VNode {
        if ( null != this.child )
            return m( this.tag.toString(), getAttributes(), this.child )
        this.children.also {
            if ( null != it )
                return m( this.tag.toString(), getAttributes(), it )
        }
        this.stringContents.also {
            if ( null != it )
                return m( this.tag.toString(), getAttributes(), it )
        }
        this.tag.also {
            if ( null != it )
                return m( it.toString(), getAttributes(), "" )
        }
        throw Error( "Unknown hyperscript format: all arguments null (*should* never get here!)" )
    }

    class Details(
        private val builder: HyperScriptBuilder
    ) {

        fun attributes( attributes: Any ) {
            this.builder.attributes.add( attributes )
        }

        fun child( child: VNode? ) {
            this.builder.child = child
        }

        fun children( children: Collection < VNode >? ) {
            this.builder.children = children?.toTypedArray()
        }

        fun children( vararg children: VNode? ) {
            this.builder.children = children.filterNotNull().toTypedArray()
        }

        fun children( vararg childCollections: Collection < VNode > ) {
            this.builder.children = childCollections.toList().flatten().toTypedArray()
        }

        fun content( stringContents: String ) {
            this.builder.stringContents = stringContents
        }

        @Suppress("UNCHECKED_CAST")
        fun eventHandlers( eventHandlerClosure: EventHandlers.() -> Unit ) {
            EventHandlers()
                .apply {
                    eventHandlerClosure()
                    builder.attributes.add( this )
                }
        }

    }

}