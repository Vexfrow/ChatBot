package fr.c1.chatbot.model.activity

class Site(
    val region: String,
    val department: String,
    commune: String,
    val accessible: Boolean,

    ) : AbstractActivity(commune) {
    companion object {
        val passions: List<String> = listOf(
            "site", "monument", "patrimoine",
            "histoire", "culture", "architecture",
            "archéologie", "art", "religion",
            "civilisation", "urbanisme", "paysage"
        )
    }

    override fun toString(): String {
        return "Site patrimonial (region='$region', departement='$department', commune='$commune', accessible='$accessible')"
    }
}