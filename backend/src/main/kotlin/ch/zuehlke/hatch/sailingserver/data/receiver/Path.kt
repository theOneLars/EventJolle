package ch.zuehlke.hatch.sailingserver.data.receiver

import java.util.*
import java.util.function.Consumer

data class Path(val elements: List<PathElement>) {

    fun forEach(action: Consumer<PathElement>) {
        Objects.requireNonNull(action)
        for (element in elements) {
            action.accept(element)
        }
    }

    fun isLast(element: PathElement): Boolean {
        return Objects.equals(element, this.elements.last())
    }

    fun getLastElement(): PathElement {
        return this.elements.last();
    }
}