package org.js.mithril

interface Component {
    @JsName( "view" )
    fun view() : VNode
}