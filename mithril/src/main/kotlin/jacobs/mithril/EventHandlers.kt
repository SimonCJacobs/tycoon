package jacobs.mithril

import org.js.mithril.LifecycleMethod
import org.w3c.dom.events.Event

/**************************************************************************************
 ***                                                                                ***
 ***  WHEN UPDATING THIS CLASS ENSURE ALL EVENTS ARE INCLUDED IN THE MERGE METHOD   ***
 ***                                                                                ***
 **************************************************************************************/
@Suppress( "MemberVisibilityCanBePrivate" )
class EventHandlers {

    /**
     * Lifecycle methods
     */

    var onupdate: LifecycleMethod? = null

    /**
     * Additional functionality
     */

    var onInputExt: EventHandler? = null

    /**
     * HTML standard
     */

    var onclick: EventHandler? = null
    var onkeyup: EventHandler? = null
    var oninput: EventHandler? = null

    fun mergeIntoJsObject( jsObject: Any ) {
        this.mergeLifecycleMethods( jsObject )
        this.mergeAdditionalFunctionalityMethods( jsObject )
        this.mergeHtmlStandardMethods( jsObject )
    }

    private fun mergeLifecycleMethods( jsObject: Any ) {
        jsObject.asDynamic().onupdate = this.onupdate
    }

    private fun mergeAdditionalFunctionalityMethods( jsObject: Any ) {
        jsObject.copyAcrossIfExists( this.onInputExt, "onclick", "oninput", "onkeyup" )
    }

    private fun mergeHtmlStandardMethods( jsObject: Any ) {
        jsObject.copyAcrossIfExists( this.onclick, "onclick"  )
        jsObject.copyAcrossIfExists( this.oninput, "oninput" )
        jsObject.copyAcrossIfExists( this.onkeyup, "onkeyup" )
    }

    private fun Any.copyAcrossIfExists( eventHandler: EventHandler?, vararg stringNames: String ) {
        stringNames.forEach {
            if ( null !== eventHandler )
                this.asDynamic()[ it ] = eventHandler
        }
    }

}

typealias EventHandler = ( Event ) -> Unit