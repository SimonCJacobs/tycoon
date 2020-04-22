package jacobs.tycoon.view.components

import jacobs.mithril.HyperScriptFactory
import jacobs.mithril.Tag
import jacobs.tycoon.domain.Utility

class UtilityComponent( private val utility: Utility )
    : PropertyComponent( utility.name ) {


}