package ch.zuehlke.hatch.sailingserver.livecache

interface TemporalIdentifier<T> {
    fun isAfter(identifier: T): Boolean
}