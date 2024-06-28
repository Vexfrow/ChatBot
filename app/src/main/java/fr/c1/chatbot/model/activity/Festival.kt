package fr.c1.chatbot.model.activity

/**
 * Represents a festival.
 * @param region the region of the festival
 * @param department the department of the festival
 * @param commune the commune of the festival
 * @param name the name of the festival
 * @param address the address of the festival
 * @param postalCode the postal code of the festival
 * @param discipline the discipline of the festival
 * @param accessible true if the festival is accessible, false otherwise
 * @param latitude the latitude of the festival
 * @param longitude the longitude of the festival
 * @constructor creates a festival
 * @see AbstractActivity
 */
class Festival(
    val region: String,
    val department: String,
    commune: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val discipline: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "musique", "danse", "théâtre", "cirque",
            "cinéma", "littérature", "arts plastiques",
            "photographie", "rire", "humour", "chant"
        )
    }

    override fun toString(): String {
        return "Festival(region='$region', department='$department', name='$name', address='$address', postalCode='$postalCode', discipline='$discipline', accessible=$accessible)"
    }
}