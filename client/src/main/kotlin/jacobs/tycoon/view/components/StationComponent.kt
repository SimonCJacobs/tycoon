package jacobs.tycoon.view.components

import jacobs.tycoon.domain.Station

class StationComponent( private val station: Station )
    : PropertyComponent ( station.name ) {


}