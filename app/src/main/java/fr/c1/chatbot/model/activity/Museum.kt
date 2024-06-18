package fr.c1.chatbot.model.activity

/**
 * Museum class represents a museum activity.
 * @param region the region of the museum
 * @param department the department of the museum
 * @param id the id of the museum
 * @param commune the commune of the museum
 * @param name the name of the museum
 * @param address the address of the museum
 * @param location the location of the museum
 * @param postalCode the postal code of the museum
 * @param phone the phone number of the museum
 * @param url the url of the museum
 * @param accessible the accessibility of the museum
 * @param latitude the latitude of the museum
 * @param longitude the longitude of the museum
 * @constructor creates a museum activity
 * @see AbstractActivity
 */
class Museum(
    val region: String,
    val department: String,
    val id: String,
    commune: String,
    val name: String,
    val address: String,
    val location: String,
    val postalCode: String,
    val phone: String,
    val url: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "musée", "exposition", "peinture",
            "sculpture", "art", "histoire",
            "archéologie", "ethnologie", "science",
            "technique", "industrie", "artisanat",
            "patrimoine", "culture", "civilisation",
            "éducation", "pédagogie", "tourisme",
            "détente", "divertissement"
        )
    }

    override fun toString(): String {
        return "Museum(region='$region', department='$department', id='$id', name='$name', address='$address', location='$location', postalCode='$postalCode', phone='$phone', url='$url', accessible=$accessible)"
    }
}