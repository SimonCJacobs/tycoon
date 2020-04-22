package jacobs.tycoon.view.components.board

import jacobs.tycoon.domain.board.Street
import jacobs.tycoon.view.components.board.PropertyComponent

class StreetComponent( private val street: Street )
    : PropertyComponent( street.name ) {

}