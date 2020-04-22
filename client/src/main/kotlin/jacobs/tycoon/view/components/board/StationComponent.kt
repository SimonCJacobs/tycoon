package jacobs.tycoon.view.components.board

import jacobs.tycoon.domain.board.Station

class StationComponent( private val station: Station )
    : PropertyComponent( station.name ) {


}