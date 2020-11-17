package jacobs.tycoon.clientstate

class AdministratableProperties {

    init {
        this.resetCashSettingProperties()
    }

    fun resetCashSettingProperties() {
        this.newCashHoldings = ""
        this.updatingCash = false
    }

    lateinit var newCashHoldings: String
    var updatingCash: Boolean = false

}