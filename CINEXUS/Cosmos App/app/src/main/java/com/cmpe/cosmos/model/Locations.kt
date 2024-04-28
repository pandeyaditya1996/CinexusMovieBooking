package com.cmpe.cosmos.model

enum class Locations(val location: String, val locationID: Int) {
    SAN_JOSE("San Jose", 2),
    SANTA_CLARA("Santa Clara", 3),
    SUNNYVALE("Sunnyvale", 4),
    FREMONT("Fremont", 5),
    CUPERTINO("Cupertino", 6),
    MILPITAS("Milpitas", 7),
    PALO_ALTO("Palo Alto", 8),
    SAN_FRANCISCO("San Francisco", 1),
    MOUNTAIN_VIEW("Mountain View", 9);

    companion object {
        private val map = Locations.values().associateBy { it.location }
        infix fun from(value: String) = map[value]
    }
}