package ch.zuehlke.hatch.sailingserver.data.eventstore

import org.bson.Document

interface EventTransformer<T> {
    fun getPath(): String
    fun transform(document: Document): List<T>
}
