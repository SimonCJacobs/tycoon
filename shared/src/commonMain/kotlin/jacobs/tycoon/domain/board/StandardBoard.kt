package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

/**
 * Source: http://www.jdawiseman.com/papers/trivia/monopoly-rents.html
 */
@Serializable
abstract class StandardBoard : Board() {

    companion object {
        const val STATION_PRICE = 200
        const val UTILITY_PRICE = 150
    }

    protected fun buildSquareList( nameList: List < String > ): List < Square > {
        return nameList.mapIndexed { index, eachName ->
            this.getNamelessSquareMapList()[ index ]( eachName ) }
    }

    private fun getNamelessSquareMapList(): List < ( String ) -> Square > {
        return listOf(

            { name -> ActionSquare( name ) },

            { name -> Street( name, 60 ) },
            { name -> ActionSquare( name ) },
            { name -> Street( name, 60 ) },
            { name -> ActionSquare( name ) },

            { name -> Station( name, STATION_PRICE ) },

            { name -> Street( name, 100 ) },
            { name -> ActionSquare( name ) },
            { name -> Street( name, 100 ) },
            { name -> Street( name, 120 ) },

            { name -> ActionSquare( name) },

            { name -> Street( name, 140 ) },
            { name -> Utility( name, UTILITY_PRICE ) },
            { name -> Street( name, 140 ) },
            { name -> Street( name, 160 ) },

            { name -> Station( name, STATION_PRICE ) },

            { name -> Street( name, 180 ) },
            { name -> ActionSquare( name ) },
            { name -> Street( name, 180 ) },
            { name -> Street( name, 200 ) },

            { name -> ActionSquare( name ) },

            { name -> Street( name, 220 ) },
            { name -> ActionSquare( name ) },
            { name -> Street( name, 220 ) },
            { name -> Street( name, 240 ) },

            { name -> Station(name, STATION_PRICE ) },

            { name -> Street( name, 260 ) },
            { name -> Street( name, 260 ) },
            { name -> Utility( name, UTILITY_PRICE ) },
            { name -> Street( name, 280 ) },

            { name -> ActionSquare( name) },

            { name -> Street( name, 300 ) },
            { name -> Street( name, 300 ) },
            { name -> ActionSquare( name ) },
            { name -> Street( name, 320 ) },

            { name -> Station( name, STATION_PRICE ) },

            { name -> ActionSquare( name ) },
            { name -> Street( name, 350 ) },
            { name -> ActionSquare( name ) },
            { name -> Street( name, 400  ) }
        )
    }
}

