package fr.c1.chatbot.model.activity

/**
 * Represents an exposition.
 * @param region the region of the exposition
 * @param department the department of the exposition
 * @param id the id of the exposition
 * @param commune the commune of the exposition
 * @param name the name of the exposition
 * @param url the url of the exposition
 * @param accessible true if the exposition is accessible, false otherwise
 * @param latitude the latitude of the exposition
 * @param longitude the longitude of the exposition
 * @constructor creates an exposition activity
 * @see AbstractActivity
 */
class Exposition(
    val region: String,
    val department: String,
    val id: String,
    commune: String,
    val name: String,
    val url: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "exposition", "musée", "peinture", "sculpture",
            "photographie", "art", "histoire", "culture",
            "patrimoine", "visite", "visite guidée", "visite libre",
            "visite commentée", "visite virtuelle", "exposition virtuelle",
        )
    }

    override fun toString(): String {
        return "Exposition(region='$region', department='$department', id='$id', name='$name', url='$url', accessible=$accessible)"
    }
}