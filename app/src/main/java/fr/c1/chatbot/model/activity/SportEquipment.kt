package fr.c1.chatbot.model.activity

class SportEquipment(
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
            "sport", "activité", "activité sportive", "activité physique"
        )
    }

    override fun toString(): String {
        return "SportEquipment(department='$department', name='$name', address='$address', postalCode='$postalCode', accessible=$accessible)"
    }
}