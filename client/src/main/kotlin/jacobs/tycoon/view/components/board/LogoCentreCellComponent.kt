package jacobs.tycoon.view.components.board

class LogoCentreCellComponent (
    private val gameName: String,
    squaresToASideExcludingCorners: Int
) : CentreCellComponent( squaresToASideExcludingCorners ) {

    override fun getFontSize(): String? {
        return "80px"
    }

    override fun getFontStyle(): String? {
        return "italic"
    }

    override fun getText(): String {
        return this.gameName
    }

    override fun getTransform(): String? {
        return "rotate( -0.048turn )"
    }

}