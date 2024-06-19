package fr.c1.chatbot.model.activity

/**
 * Represents a cultural content.
 * A content can be a museum, a library, a cinema, a concert hall, etc.
 * It is a place where cultural activities take place.
 * @param id the unique identifier of the content
 * @param commune the commune where the content is located
 * @param name the name of the content
 * @param address the address of the content
 * @param location the location of the content
 * @param postalCode the postal code of the content
 * @param url the URL of the content
 * @param accessible true if the content is accessible, false otherwise
 * @param latitude the latitude of the content
 * @param longitude the longitude of the content
 * @constructor creates a cultural content
 * @see AbstractActivity
 */
class Content(
    val id: String,
    commune: String,
    val name: String,
    val address: String,
    val location: String,
    val postalCode: String,
    val url: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "culture", "art", "archives", "bibliothèque",
            "cinéma", "concert", "orchestre", "musique",
            "conférence", "danse", "exposition", "festival",
            "littérature", "musée", "patrimoine", "théâtre"
        )
    }

    override fun toString(): String {
        return "Content(id='$id', name='$name', address='$address', location='$location', postalCode='$postalCode', url='$url', accessible=$accessible)"
    }
}