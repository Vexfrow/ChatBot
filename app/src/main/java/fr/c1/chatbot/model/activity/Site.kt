package fr.c1.chatbot.model.activity

/**
 * A patrimonial site that can be visited.
 * @param region the region where the site is located
 * @param department the department where the site is located
 * @param commune the commune where the site is located
 * @param accessible true if the site is accessible to people with reduced mobility
 * @param latitude the latitude of the site
 * @param longitude the longitude of the site
 * @constructor creates a site with the given parameters
 * @see AbstractActivity
 */
class Site(
    val region: String,
    val department: String,
    commune: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "site", "monument", "patrimoine",
            "histoire", "culture", "architecture",
            "archéologie", "art", "religion",
            "civilisation", "urbanisme", "paysage"
        )
    }

    override fun toString(): String {
        return "Site(region='$region', department='$department', accessible=$accessible)"
    }
}