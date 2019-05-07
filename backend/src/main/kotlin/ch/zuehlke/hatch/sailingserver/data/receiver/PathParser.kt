package ch.zuehlke.hatch.sailingserver.data.receiver

import java.util.stream.Collectors

internal class PathParser {

    fun parse(path: String): Path {
        val elements = path.split('.');

        val pathElements = elements.stream()
                .map { this.toPathElement(it) }
                .collect(Collectors.toList())

        return Path(pathElements)
    }

    private fun toPathElement(element: String): PathElement {
        if (element.endsWith("]")) {
            val name = element.substring(0, element.indexOf('['))
            val index = element.substring(element.indexOf('[') + 1, element.indexOf(']'))

            return PathElement(name, Integer.parseInt(index))
        } else {
            return PathElement(element, null)
        }
    }
}