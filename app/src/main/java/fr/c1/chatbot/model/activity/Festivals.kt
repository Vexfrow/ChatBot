package fr.c1.chatbot.model.activity

class Festivals(
    val region: String,
    val departement: String,
    commune: String,
    val nom: String,
    val adresse: String,
    val codePostal: String,
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
        return "Festival (region='$region', departement='$departement', commune='$commune', nom='$nom', adresse='$adresse', codePostal='$codePostal', accessible='$accessible', discipline='$discipline', latitude=$latitude, longitude=$longitude)"
    }
}