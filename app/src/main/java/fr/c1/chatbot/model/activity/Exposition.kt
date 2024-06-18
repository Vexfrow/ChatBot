package fr.c1.chatbot.model.activity

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