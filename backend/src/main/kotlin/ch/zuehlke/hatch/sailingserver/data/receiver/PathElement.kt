package ch.zuehlke.hatch.sailingserver.data.receiver

data class PathElement(val name: String, val index: Int?) {

    fun isArray(): Boolean {
        return index != null
    }
}