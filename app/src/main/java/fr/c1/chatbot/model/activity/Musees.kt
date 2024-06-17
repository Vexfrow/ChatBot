package fr.c1.chatbot.model.activity

class Musees(
    val region: String,
    val departement: String,
    val identifiant: String,
    commune: String,
    val nom: String,
    val adresse: String,
    val lieu: String,
    val codePostal: String,
    val telephone: String,
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
        return "Musée (region='$region', departement='$departement', identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', lieu='$lieu', codePostal='$codePostal', telephone='$telephone', url='$url', accessible='$accessible', latitude='$latitude', longitude='$longitude')"
    }
}