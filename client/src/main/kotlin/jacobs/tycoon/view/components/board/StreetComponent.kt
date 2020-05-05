package jacobs.tycoon.view.components.board

import jacobs.tycoon.domain.board.Street

class StreetComponent( private val street: Street )
    : PropertyComponent( street.name ) {
}