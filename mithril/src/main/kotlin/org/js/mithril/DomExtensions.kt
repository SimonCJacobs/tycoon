package org.js.mithril

import jacobs.mithril.EventHandler
import org.w3c.dom.HTMLSelectElement

fun selectedIndex( indexLambda: ( Int ) -> Unit  ): EventHandler = { event ->
    ( event.target as HTMLSelectElement ).selectedIndex
        .let { indexLambda( it ) }
}