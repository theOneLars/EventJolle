package ch.zuehlke.hatch.sailingserver.livecache

class ReadPointer<T: TemporalIdentifier<T>> {

    private var lastRead: T? = null

    fun isNewer(temporalIdentifier: T): Boolean {
        synchronized(ReadPointer::class) {
            return lastRead == null || temporalIdentifier.isAfter(lastRead!!)
        }
    }

    fun read(temporalIdentifier: T): Boolean {
        synchronized(ReadPointer::class) {
            if (this.isNewer(temporalIdentifier)) {
                lastRead = temporalIdentifier
                return true
            }
            return false
        }
    }

    fun reset() {
        synchronized(ReadPointer::class) {
            lastRead = null
        }
    }
}