package external.mithril

interface ExtractionRequestOptions < T > : SimpleRequestOptions {
    val deserialize: ( data: String ) -> T?
}
