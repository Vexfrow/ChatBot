package fr.c1.chatbot.model.activity

class Sites(
    val region: String,
    val departement: String,
    val commune: String,
    val accessible: Boolean,
    passions: List<String> = listOf(
        "site", "monument", "patrimoine",
        "histoire", "culture", "architecture",
        "archéologie", "art", "religion",
        "civilisation", "urbanisme", "paysage"
    )
) : AbstractActivity() {

    override fun toString(): String {
        return "Site patrimonial (region='$region', departement='$departement', commune='$commune', accessible='$accessible')"
    }
}