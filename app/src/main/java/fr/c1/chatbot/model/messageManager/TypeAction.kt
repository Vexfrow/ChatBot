package fr.c1.chatbot.model.messageManager

enum class TypeAction {
    /** No specific action (searchbar disabled) */
    None,

    /** Date picker */
    DateInput,

    /** Int */
    DistanceInput,

    /** Dropdown */
    CityInput,

    /** Result Tab */
    ShowResults,

    /** Get the Android last location */
    Geolocation,

    /** Moved on another tab */
    ChoosePassions,

    /** Keep physical activities */
    PhysicalActivity,

    /** Keep cultural activities */
    CulturalActivity,

    /** Disable the notification */
    DeleteNotifs,


    /** Sport (no changes) **/
    BigSportif,
    LittleSportif,
    InexistantSportif,

    /** Meeting (no changes) **/
    Meeting,
    NoMeeting,
    PerhapsMeeting,

    /** For the date **/
    Today,
    Tomorrow,

    /** Undo last message */
    Back,
    /** Restart chat */
    Restart,
    /** Show the filters */
    ShowFilters,
}