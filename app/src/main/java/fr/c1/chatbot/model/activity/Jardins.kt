package fr.c1.chatbot.model.activity

class Jardins(
    val region: String,
    val departement: String,
    commune: String,
    val nom: String,
    val adresse: String,
    val codePostal: String,
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
        return "Jardin remarquable (region='$region', departement='$departement', commune='$commune', nom='$nom', adresse='$adresse', codePostal='$codePostal', accessible='$accessible', latitude=$latitude, longitude=$longitude)"
    }
}