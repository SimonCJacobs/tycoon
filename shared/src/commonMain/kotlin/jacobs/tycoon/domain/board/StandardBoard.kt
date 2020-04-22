package jacobs.tycoon.domain.board

abstract class StandardBoard : Board() {

    companion object {
        const val STATION_PRICE = 200
        const val UTILITY_PRICE = 200
    }

    protected abstract val nameList: List < String >

    private val namelessSquareLambdas: List < ( String ) -> Square> = listOf(

        { name -> ActionSquare(name) },

        { name -> Street(name, 60) },
        { name -> ActionSquare(name) },
        { name -> Street(name, 60) },
        { name -> ActionSquare(name) },

        { name -> Station(name, STATION_PRICE) },

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

        // Lazy evaluation allows the property in the child object to have been created
        // before using it
    override val squareList: List <Square> by lazy {
        this.nameList.mapIndexed { index, eachName ->
            this.namelessSquareLambdas[ index ]( eachName ) }
        }

}

