package fr.c1.chatbot.model.activity

class Association(
    val department: String,
    val id: String,
    commune: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double,
    val url: String
) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "association", "bénévolat", "engagement", "solidarité",
            "humanitaire", "social", "culture", "sport",
            "environnement", "éducation", "santé", "handicap",
            "économie", "emploi", "formation", "insertion",
            "logement", "justice", "citoyenneté", "développement",
            "international", "numérique", "innovation", "égalité",
            "diversité", "inclusion", "démocratie", "participation",
            "coopération", "échanges", "mobilité", "communication",
            "information", "médiation", "animation", "événementiel",
            "formation", "séminaire", "colloque", "visite",
            "rencontre", "échange", "partage", "découverte",
            "initiation", "santé", "prévention", "accompagnement", "écoute"
        )
    }

    override fun toString(): String {
        return "Association(department='$department', id='$id', name='$name', address='$address', postalCode='$postalCode', accessible=$accessible, url='$url')"
    }
}