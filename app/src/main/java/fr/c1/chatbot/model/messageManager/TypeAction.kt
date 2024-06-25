package fr.c1.chatbot.model.messageManager

enum class TypeAction {
    /** No specific action (searchbar disabled) */
    None,

    /** Date picker */
    DateInput,

    /** Int */
    DistanceInput,

    /** Dropdown with auto completion */
    CityInput,

    /** Result Tab */
    ShowResults,

    /** ToDo: city ? */
    Geolocate,

    /** Dropdown with multiple choices */
    ChoosePassions,

    /** Just a suggestion */
    PhysicalActivity,

    /** Just a suggestion */
    CulturalActivity,

    DeleteNotifs,

    BigSportif,

    LittleSportif,

    InexistantSportif,

    /** Just a suggestion */
    Back,
    Restart,
    ShowFilters,
}