package jacobs.tycoon.view.components

import external.mithril.VNode
import jacobs.mithril.HyperScriptFactory
import jacobs.mithril.Tag
import jacobs.tycoon.domain.Station
import jacobs.tycoon.domain.Street

class StationComponent( private val m: HyperScriptFactory, private val station: Station )
    : PropertyComponent ( m, station.name ) {


}