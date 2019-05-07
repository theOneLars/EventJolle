package ch.zuehlke.hatch.sailingserver.data

import org.springframework.stereotype.Component

class CollectionNames {

    private val collectionNames = mapOf("navigation.position" to COLLECTION_NAME_EVENTS_NAVIGATION_POSITION)

    companion object {
        val COLLECTION_NAME_EVENTS_NAVIGATION_POSITION = "events-navigation-position";
    }

    fun getByPath(path: String): String {
        return this.collectionNames.getOrDefault(path, "events-other");
    }
}