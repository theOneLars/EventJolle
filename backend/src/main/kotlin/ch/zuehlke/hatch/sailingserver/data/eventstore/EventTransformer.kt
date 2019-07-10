package ch.zuehlke.hatch.sailingserver.data.eventstore

import com.google.gson.JsonObject

interface EventTransformer<T> {
    fun getPath(): String

    fun transform(json: JsonObject): List<T>
}
