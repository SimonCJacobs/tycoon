package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class StandardBoard : Board() {

    protected fun buildSquareList( nameList: List < String > ): List < Square > {
        return nameList.mapIndexed { index, eachName ->
            this.getNamelessSquareMapList()[ index ]( eachName ) }
    }

    private fun getNamelessSquareMapList(): List < ( String ) -> Square > {
        return listOf(

            { name -> ActionSquare(name) },

            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },

            { name -> Station( name, LondonBoard.STATION_PRICE) },

            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> ActionSquare(name) },

            { name -> Street(name, 60) },
            { name -> Utility(name, LondonBoard.UTILITY_PRICE) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> Station(name, LondonBoard.STATION_PRICE) },

            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> ActionSquare(name) },

            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> Station(name, LondonBoard.STATION_PRICE) },

            { name -> Street(name, 60) },
            { name -> Street(name, 60) },
            { name -> Utility(name, LondonBoard.UTILITY_PRICE) },
            { name -> Street(name, 60) },

            { name -> ActionSquare(name) },

            { name -> Street(name, 60) },
            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },

            { name -> Station(name, LondonBoard.STATION_PRICE) },

            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) }
        )
    }
}

