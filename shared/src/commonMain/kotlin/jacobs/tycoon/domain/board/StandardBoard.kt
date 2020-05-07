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

            { name -> GoSquare( name ) },

            { name -> Street( name, 60 ) },
            { name -> CardSquare( name ) },
            { name -> Street( name, 60 ) },
            { name -> TaxSquare( name ) },

            { name -> Station( name, STATION_PRICE ) },

            { name -> Street( name, 100 ) },
            { name -> CardSquare( name ) },
            { name -> Street( name, 100 ) },
            { name -> Street( name, 120 ) },

            { _ -> JustVisitingJailSquare() },

            { name -> Street( name, 140 ) },
            { name -> Utility( name, UTILITY_PRICE ) },
            { name -> Street( name, 140 ) },
            { name -> Street( name, 160 ) },

            { name -> Station( name, STATION_PRICE ) },

            { name -> Street( name, 180 ) },
            { name -> CardSquare( name ) },
            { name -> Street( name, 180 ) },
            { name -> Street( name, 200 ) },

            { name -> FreeParkingSquare( name ) },

            { name -> Street( name, 220 ) },
            { name -> CardSquare( name ) },
            { name -> Street( name, 220 ) },
            { name -> Street( name, 240 ) },

            { name -> Station(name, STATION_PRICE ) },

            { name -> Street( name, 260 ) },
            { name -> Street( name, 260 ) },
            { name -> Utility( name, UTILITY_PRICE ) },
            { name -> Street( name, 280 ) },

            { _ -> GoToJailSquare() },

            { name -> Street( name, 300 ) },
            { name -> Street( name, 300 ) },
            { name -> CardSquare( name ) },
            { name -> Street( name, 320 ) },

            { name -> Station( name, STATION_PRICE ) },

            { name -> CardSquare( name ) },
            { name -> Street( name, 350 ) },
            { name -> TaxSquare( name ) },
            { name -> Street( name, 400  ) }
        )
    }
}

