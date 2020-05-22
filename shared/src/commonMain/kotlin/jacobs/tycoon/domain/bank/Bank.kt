package jacobs.tycoon.domain.bank

import jacobs.tycoon.domain.rules.MiscellaneousRules

class Bank(
    miscellaneousRules: MiscellaneousRules
) {

    val interestRate: Float = 0.1F
    var housesInStock = miscellaneousRules.initialHousingStock
    var hotelsInStock = miscellaneousRules.initialHotelStock
    val housesToAHotel = miscellaneousRules.housesToAHotel

}