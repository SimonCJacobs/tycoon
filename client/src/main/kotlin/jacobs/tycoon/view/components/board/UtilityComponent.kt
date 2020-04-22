package jacobs.tycoon.view.components.board

import jacobs.tycoon.domain.board.Utility
import jacobs.tycoon.view.components.board.PropertyComponent

class UtilityComponent( private val utility: Utility )
    : PropertyComponent( utility.name ) {


}