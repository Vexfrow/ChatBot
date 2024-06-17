package fr.c1.chatbot.model.activity

class EquipementsSport(
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
            "sport", "activité", "activité sportive", "activité physique"
        )
    }

    override fun toString(): String {
        return "Equipement sportif (departement='$departement', commune='$commune', nom='$nom', adresse='$adresse', codePostal='$codePostal', accessible='$accessible', latitude='$latitude', longitude='$longitude')"
    }
}