package jacobs.tycoon.view.components

import jacobs.tycoon.domain.Street

class StreetComponent( private val street: Street )
    : PropertyComponent( street.name ) {

}