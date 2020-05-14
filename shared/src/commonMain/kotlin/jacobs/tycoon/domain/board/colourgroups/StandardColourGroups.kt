package jacobs.tycoon.domain.board.colourgroups

import jacobs.tycoon.domain.board.currency.Currency

@Suppress( "PropertyName" )
class StandardColourGroups private constructor(
    currency: Currency
) {

    companion object {
        private var instance: StandardColourGroups? = null
        fun instance( currency: Currency ): StandardColourGroups {
            if ( null == instance )
                this.instance = StandardColourGroups( currency )
            return this.instance!!
        }
    }

    val BROWN = ColourGroup( "brown", 2, 50, currency )
    val LIGHT_BLUE = ColourGroup( "lightBlue", 3, 50, currency )
    val PINK = ColourGroup( "pink", 3, 100, currency )
    val ORANGE = ColourGroup( "orange", 3, 100, currency )
    val RED = ColourGroup( "red", 3, 150, currency )
    val YELLOW = ColourGroup( "yellow", 3, 150, currency )
    val GREEN = ColourGroup( "green", 3, 200, currency )
    val DARK_BLUE = ColourGroup( "darkBlue", 2, 200, currency )

}