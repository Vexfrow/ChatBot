package fr.c1.chatbot.model.activity

class SportEquipment(
    val department: String,
    val id: String,
    commune: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val url: String,
    val accessible: Boolean,

    ) : AbstractActivity(commune) {
    companion object {
        val passions: List<String> = listOf(
            "sport", "activité", "activité sportive", "activité physique"
        )
    }

    override fun toString(): String {
        return "Equipement sportif (departement='$department', identifiant='$id', commune='$commune', nom='$name', adresse='$address', codePostal='$postalCode', url='$url', accessible='$accessible')"
    }
}