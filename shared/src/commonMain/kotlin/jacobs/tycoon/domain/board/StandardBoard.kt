package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.colourgroups.StandardColourGroups
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.FreeParkingSquare
import jacobs.tycoon.domain.board.squares.GoSquare
import jacobs.tycoon.domain.board.squares.GoToJailSquare
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.JustVisitingJailSquare
import jacobs.tycoon.domain.board.squares.RentCard
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.TaxSquare
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.domain.board.squares.UtilityMultipliers
import jacobs.tycoon.domain.rules.MiscellaneousRules
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Source: http://www.jdawiseman.com/papers/trivia/monopoly-rents.html
 */
@Serializable
abstract class StandardBoard : Board() {

    companion object {
        const val BASE_STATION_RENT = 25
        const val CHANCE_CARDS_NAME = "CHANCE"
        const val COMMUNITY_CHEST_CARDS_NAME = "COMMUNITY_CHEST"
        const val STATION_PRICE = 200
        const val UTILITY_PRICE = 150
        val UTILITY_MULTIPLIERS_PAIR = 4 to 10
    }

    @Transient override var squareList: List < Square > = emptyList()

    @Transient override var goSquare: GoSquare = GoSquare.NULL
    @Transient override var jailSquare: JailSquare = JailSquare.NULL
    @Transient override var maryleboneStation: Station = Station.NULL
    @Transient override var mayfair: Street = Street.NULL
    @Transient override var oldKentRoad: Street = Street.NULL
    @Transient override var pallMall: Street = Street.NULL
    @Transient override var trafalgarSquare: Street = Street.NULL

    @Transient private var cardSetCounter = 0

    override fun initialise( gameRules: MiscellaneousRules ) {
        this.initialiseSquares( gameRules )
        this.initialiseCardSets( gameRules )
    }

    private fun initialiseSquares( gameRules: MiscellaneousRules ) {
        this.squareList = this.nameListProvider().mapIndexed { index, eachName ->
            this.getNamelessSquareMapList( gameRules.housesToAHotel )[ index ]( index, eachName ) }
    }

    abstract fun initialiseCardSets( gameRules: MiscellaneousRules )

    private fun colourGroups(): StandardColourGroups {
        return StandardColourGroups.instance( currency )
    }

    protected abstract fun nameListProvider(): List < String >

    private fun utilityMultipliers(): UtilityMultipliers {
        return UtilityMultipliers.new( UTILITY_MULTIPLIERS_PAIR, currency )
    }

    private fun getNamelessSquareMapList( housesToAHotel: Int ): List < ( Int, String ) -> Square > {
        return listOf(

            // FIRST ROW

            { index, name ->
                goSquare = GoSquare( indexOnBoard = index, name = name, creditAmount = 200.toCurrency() )
                goSquare
            },

            { index, name ->
                oldKentRoad =
                    Street(
                        indexOnBoard = index,
                        name = name,
                        listPrice = 60.toCurrency(),
                        rentCard = RentCard( listOf( 2, 10, 30, 90, 160, 250 ), currency ),
                        colourGroup = colourGroups().BROWN,
                        housesToAHotel = housesToAHotel
                    )
                oldKentRoad
            },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSetName = COMMUNITY_CHEST_CARDS_NAME
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 60.toCurrency(),
                    rentCard = RentCard( listOf( 4, 20, 60, 180, 320, 450 ), currency ),
                    colourGroup = colourGroups().BROWN,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name -> TaxSquare( indexOnBoard = index, name = name, taxCharge = 200.toCurrency()) },

            { index, name ->
                Station(
                    indexOnBoard = index,
                    name = name,
                    listPrice = STATION_PRICE.toCurrency(),
                    singleStationRent = BASE_STATION_RENT.toCurrency()
                )
            },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 100.toCurrency(),
                    rentCard = RentCard( listOf( 6, 30, 90, 270, 400, 550 ), currency ),
                    colourGroup = colourGroups().LIGHT_BLUE,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSetName = CHANCE_CARDS_NAME
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 100.toCurrency(),
                    rentCard = RentCard( listOf( 6, 30, 90, 270, 400, 550 ), currency ),
                    colourGroup = colourGroups().LIGHT_BLUE,
                    housesToAHotel = housesToAHotel

                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 120.toCurrency(),
                    rentCard = RentCard( listOf( 8, 40, 100, 300, 450, 600 ), currency ),
                    colourGroup = colourGroups().LIGHT_BLUE,
                    housesToAHotel = housesToAHotel
                ) },

            { index, _ -> JustVisitingJailSquare( indexOnBoard = index )
                            .also { this.jailSquare = JailSquare( it.indexOnBoard ) }
            },

            // END OF FIRST ROW
            // LEFT-HAND SIDE

            { index, name ->
                pallMall =
                    Street(
                        indexOnBoard = index,
                        name = name,
                        listPrice = 140.toCurrency(),
                        rentCard = RentCard( listOf( 10, 50, 150, 450, 625, 750 ), currency ),
                        colourGroup = colourGroups().PINK,
                        housesToAHotel = housesToAHotel
                    )
                pallMall
            },
            { index, name ->
                Utility(
                    indexOnBoard = index,
                    name = name,
                    listPrice = UTILITY_PRICE.toCurrency(),
                    utilityMultipliers = utilityMultipliers()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 140.toCurrency(),
                    rentCard = RentCard( listOf( 10, 50, 150, 450, 625, 750 ), currency ),
                    colourGroup = colourGroups().PINK,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 160.toCurrency(),
                    rentCard = RentCard( listOf( 12, 60, 180, 500, 700, 900 ), currency ),
                    colourGroup = colourGroups().PINK,
                    housesToAHotel = housesToAHotel
                ) },

            { index, name ->
                maryleboneStation =
                    Station(
                        indexOnBoard = index,
                        name = name,
                        listPrice = STATION_PRICE.toCurrency(),
                        singleStationRent = BASE_STATION_RENT.toCurrency()
                    )
                maryleboneStation
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 180.toCurrency(),
                    rentCard = RentCard( listOf( 14, 70, 200, 550, 750, 950 ), currency ),
                    colourGroup = colourGroups().ORANGE,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSetName = COMMUNITY_CHEST_CARDS_NAME
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 180.toCurrency(),
                    rentCard = RentCard( listOf( 14, 70, 200, 550, 750, 950 ), currency ),
                    colourGroup = colourGroups().ORANGE,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 200.toCurrency(),
                    rentCard = RentCard( listOf( 16, 80, 220, 600, 8000, 1000 ), currency ),
                    colourGroup = colourGroups().ORANGE,
                    housesToAHotel = housesToAHotel
                ) },

            { index, name -> FreeParkingSquare( indexOnBoard = index, name = name ) },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 220.toCurrency(),
                    rentCard = RentCard( listOf( 18, 90, 250, 700, 875, 1050 ), currency ),
                    colourGroup = colourGroups().RED,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSetName = CHANCE_CARDS_NAME
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 220.toCurrency(),
                    rentCard = RentCard( listOf( 18, 90, 250, 700, 875, 1050 ), currency ),
                    colourGroup = colourGroups().RED,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                trafalgarSquare =
                    Street(
                        indexOnBoard = index,
                        name = name,
                        listPrice = 240.toCurrency(),
                        rentCard = RentCard( listOf( 20, 100, 300, 750, 925, 1100 ), currency ),
                        colourGroup = colourGroups().RED,
                        housesToAHotel = housesToAHotel
                    )
                trafalgarSquare
            },

            { index, name ->
                Station(
                    indexOnBoard = index,
                    name = name,
                    listPrice = STATION_PRICE.toCurrency(),
                    singleStationRent = BASE_STATION_RENT.toCurrency()
                )
            },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 260.toCurrency(),
                    rentCard = RentCard( listOf( 22, 110, 330, 800, 975, 1150 ), currency ),
                    colourGroup = colourGroups().YELLOW,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 260.toCurrency(),
                    rentCard = RentCard( listOf( 22, 110, 330, 800, 975, 1150 ), currency ),
                    colourGroup = colourGroups().YELLOW,
                    housesToAHotel = housesToAHotel
                ) },
            { index, name ->
                Utility(
                    indexOnBoard = index,
                    name = name,
                    listPrice = UTILITY_PRICE.toCurrency(),
                    utilityMultipliers = utilityMultipliers()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 280.toCurrency(),
                    rentCard = RentCard( listOf( 24, 120, 360, 850, 1025, 1200 ), currency ),
                    colourGroup = colourGroups().YELLOW,
                    housesToAHotel = housesToAHotel
                )
            },

            { index, _ -> GoToJailSquare( indexOnBoard = index ) },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 300.toCurrency(),
                    rentCard = RentCard( listOf( 26, 130, 390, 900, 1100, 1275 ), currency ),
                    colourGroup = colourGroups().GREEN,
                    housesToAHotel = housesToAHotel
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 300.toCurrency(),
                    rentCard = RentCard( listOf( 26, 130, 390, 900, 1100, 1275 ), currency ),
                    colourGroup = colourGroups().GREEN,
                    housesToAHotel = housesToAHotel
                )
            },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSetName = COMMUNITY_CHEST_CARDS_NAME
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 320.toCurrency(),
                    rentCard = RentCard( listOf( 28, 150, 450, 1000, 1200, 1400 ), currency ),
                    colourGroup = colourGroups().GREEN,
                    housesToAHotel = housesToAHotel
                )
            },

            { index, name ->
                Station(
                    indexOnBoard = index,
                    name = name,
                    listPrice = STATION_PRICE.toCurrency(),
                    singleStationRent = BASE_STATION_RENT.toCurrency()
                )
            },

            {
                index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSetName = CHANCE_CARDS_NAME
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 350.toCurrency(),
                    rentCard = RentCard( listOf( 35, 175, 500, 1100, 1300, 1500 ), currency ),
                    colourGroup = colourGroups().DARK_BLUE,
                    housesToAHotel = housesToAHotel
                )
            },
            {
                index, name -> TaxSquare( indexOnBoard = index, name = name, taxCharge = 150.toCurrency() )
            },
            { index, name ->
                mayfair =
                    Street(
                        indexOnBoard = index,
                        name = name,
                        listPrice = 400.toCurrency(),
                        rentCard = RentCard( listOf( 50, 200, 600, 1400, 1700, 2000 ), currency ),
                        colourGroup = colourGroups().DARK_BLUE,
                        housesToAHotel = housesToAHotel
                    )
                mayfair
            }
        )
    }

}

