package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class StandardBoard : Board() {

    companion object {
        const val STATION_PRICE = 200
        const val UTILITY_PRICE = 200
    }

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

            { name -> Station( name, STATION_PRICE) },

            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> ActionSquare(name) },

            { name -> Street(name, 60) },
            { name -> Utility(name, UTILITY_PRICE) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> Station(name, STATION_PRICE) },

            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> ActionSquare(name) },

            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> Street(name, 60) },

            { name -> Station(name, STATION_PRICE) },

            { name -> Street(name, 60) },
            { name -> Street(name, 60) },
            { name -> Utility(name, UTILITY_PRICE) },
            { name -> Street(name, 60) },

            { name -> ActionSquare(name) },

            { name -> Street(name, 60) },
            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },

            { name -> Station(name, STATION_PRICE) },

            { name -> ActionSquare(name) },
            { name -> Street(name, 60) },
            { name -> ActionSquare(name) },
            { name -> Street(name, 60) }
        )
    }
}

