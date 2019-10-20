package bell.assignment.simpletwitterclient.managers.location.model

enum class LocationSource(val sourceType: String) {
    GPS_PROVIDER("gps"),
    NETWORK_PROVIDER("network"),
    PASSIVE_PROVIDER("passive"),
    ALL("all");
}