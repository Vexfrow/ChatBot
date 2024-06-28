package fr.c1.chatbot.model.activity

/**
 * Garden class represents a garden activity.
 * @param region the region of the garden
 * @param department the department of the garden
 * @param commune the commune of the garden
 * @param name the name of the garden
 * @param address the address of the garden
 * @param postalCode the postal code of the garden
 * @param accessible the accessibility of the garden
 * @param latitude the latitude of the garden
 * @param longitude the longitude of the garden
 * @constructor creates a garden activity
 * @see AbstractActivity
 */
class Garden(
    val region: String,
    val department: String,
    commune: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "jardin", "fleurs", "plantes",
            "nature", "botanique", "horticulture",
            "paysage", "jardinage", "verger",
            "potager", "parc", "arboretum", "roseraie",
            "agriculture", "biologie", "écologie",
            "flore", "faune", "biodiversité"
        )
    }

    override fun toString(): String {
        return "Garden(region='$region', department='$department', name='$name', address='$address', postalCode='$postalCode', accessible=$accessible)"
    }
}