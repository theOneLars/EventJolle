package ch.zuehlke.hatch.sailingserver.data

import org.bson.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DocumentPropertyAccessor(private val document: Document) {

    fun getDouble(path: String): Double {
        val pathElements = path.split('.')

        val currentDocument = get(pathElements)

        return currentDocument.getDouble(pathElements.last());
    }

    fun getString(path: String): String {
        val pathElements = path.split('.')

        val currentDocument = get(pathElements)

        return currentDocument.getString(pathElements.last())
    }

    fun getTimestamp(path: String): LocalDateTime {
        return LocalDateTime.parse(getString(path), DateTimeFormatter.ISO_DATE_TIME)
    }

    private fun get(pathElements: List<String>): Document {
        var currentDocument = document;

        for (pathElement in pathElements.subList(0, pathElements.size - 1)) {
            if (pathElement.endsWith("[0]")) {
                val name = pathElement.substring(0, pathElement.indexOf('['))

                currentDocument = (currentDocument[name] as List<Document>)[0]
            } else {
                currentDocument = currentDocument[pathElement] as Document
            }
        }
        return currentDocument
    }
}