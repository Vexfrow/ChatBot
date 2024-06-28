package fr.c1.chatbot.model.activity

/**
 * Represents an association.
 * @param department the department where the association is located
 * @param id the unique identifier of the association
 * @param commune the commune where the association is located
 * @param name the name of the association
 * @param address the address of the association
 * @param postalCode the postal code of the association
 * @param accessible true if the association is accessible, false otherwise
 * @param latitude the latitude of the association
 * @param longitude the longitude of the association
 * @param url the URL of the association
 * @constructor creates an association
 * @see AbstractActivity
 */
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