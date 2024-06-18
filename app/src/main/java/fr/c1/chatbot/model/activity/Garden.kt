package fr.c1.chatbot.model.activity

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