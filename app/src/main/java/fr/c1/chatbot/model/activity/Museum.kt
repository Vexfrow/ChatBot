package fr.c1.chatbot.model.activity

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

    ) : AbstractActivity(commune) {
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
        return "Musée (region='$region', departement='$department', identifiant='$id', commune='$commune', nom='$name', adresse='$address', lieu='$location', codePostal='$postalCode', telephone='$phone', url='$url', accessible='$accessible')"
    }
}