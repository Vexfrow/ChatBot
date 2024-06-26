package fr.c1.chatbot.model.activity

/**
 * Represents a sport equipment.
 * @param department the department of the sport equipment
 * @param commune the commune of the sport equipment
 * @param name the name of the sport equipment
 * @param address the address of the sport equipment
 * @param postalCode the postal code of the sport equipment
 * @param accessible true if the sport equipment is accessible, false otherwise
 * @param latitude the latitude of the sport equipment
 * @param longitude the longitude of the sport equipment
 * @constructor creates a sport equipment
 * @see AbstractActivity
 */
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