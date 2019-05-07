package ch.zuehlke.hatch.sailingserver.data.receiver

import org.bson.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DocumentPropertyAccessor(private val document: Document) {

    private val pathParser = PathParser()

    fun containsPath(path: String): Boolean {
        var currentDocument = document

        val parsedPath = this.pathParser.parse(path)
        for (element in parsedPath.elements) {
            if (parsedPath.isLast(element)) {
                return currentDocument.containsKey(element.name)
            } else if (element.isArray()) {
                if (!currentDocument.containsKey(element.name)) {
                    return false
                } else {
                    currentDocument = (currentDocument[element.name] as List<Document>)[element.index!!]
                }
            } else {
                if (!currentDocument.containsKey(element.name)) {
                    return false
                } else {
                    currentDocument = currentDocument[element.name] as Document
                }
            }
        }

        return false
    }

    fun getDouble(path: String): Double {
        val parsedPath = this.pathParser.parse(path)

        val selectedDocument = getDocument(parsedPath)

        return selectedDocument.getDouble(parsedPath.getLastElement().name)
    }

    fun getString(path: String): String {
        val parsedPath = this.pathParser.parse(path)

        val selectedDocument = getDocument(parsedPath)

        return selectedDocument.getString(parsedPath.getLastElement().name)
    }

    fun getTimestamp(path: String): LocalDateTime {
        return LocalDateTime.parse(getString(path), DateTimeFormatter.ISO_DATE_TIME)
    }

    private fun getDocument(path: Path): Document {
        var currentDocument = document

        for (element in path.elements.subList(0, path.elements.size - 1)) {
            if (path.isLast(element)) {
                return currentDocument
            } else if (element.isArray()) {
                currentDocument = (currentDocument[element.name] as List<Document>)[element.index!!]
            } else {
                currentDocument = currentDocument[element.name] as Document
            }
        }

        return currentDocument
    }
}