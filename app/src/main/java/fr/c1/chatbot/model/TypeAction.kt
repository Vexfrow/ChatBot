package fr.c1.chatbot.model

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

    DeleteRecalls,

    DeleteSuggestions,

    DeletNotifs,

    /** Just a suggestion */
    Back,
    Restart,
    ShowFilters,
    Skip
}