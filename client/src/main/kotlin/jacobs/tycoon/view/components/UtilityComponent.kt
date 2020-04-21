package jacobs.tycoon.view.components

import external.mithril.VNode
import jacobs.mithril.HyperScriptFactory
import jacobs.mithril.Tag
import jacobs.tycoon.domain.Station
import jacobs.tycoon.domain.Street
import jacobs.tycoon.domain.Utility

class UtilityComponent( private val m: HyperScriptFactory, private val utility: Utility )
    : PropertyComponent( m, utility.name ) {


}