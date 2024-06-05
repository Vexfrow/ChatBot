package fr.c1.chatbot.model

enum class TypeAction {
    /** No specific action (searchbar disabled) */
    None,

    /** Date picker */
    EntrerDate,

    /** Int */
    EntrerDistance,

    /** Dropdown with auto completion */
    EntrerVille,

    /** Result Tab */
    AfficherResultat,

    /** ToDo: city ? */
    Geolocalisation,

    /** Dropdown with multiple choices */
    ChoisirPassions,

    /** Just a suggestion */
    ActivitePhysique,

    /** Just a suggestion */
    ActiviteCulturelle,

    /** Just a suggestion */
    Retour,
    Recommencer,
    AfficherFiltres
}