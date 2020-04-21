package jacobs.tycoon.view.components

import external.mithril.VNode
import jacobs.mithril.HyperScriptFactory
import jacobs.mithril.Tag
import jacobs.tycoon.domain.Street

class StreetComponent( m: HyperScriptFactory, street: Street )
    : PropertyComponent( m, street.name ) {

}