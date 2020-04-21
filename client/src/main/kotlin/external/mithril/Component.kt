package external.mithril

interface Component {
    @JsName( "view" )
    fun view() : VNode
}