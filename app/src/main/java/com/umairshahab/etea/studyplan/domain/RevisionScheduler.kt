package com.umairshahab.etea.studyplan.domain

import com.umairshahab.etea.studyplan.data.local.RevisionEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object RevisionScheduler {

    const val ALERT_LEAD_MILLIS = 2 * 60 * 1000L
    const val DEFAULT_INTERVALS = "3,7,14,21,30,45,60,90,120,180,365"

    fun parseIntervals(text: String): List<Int> =
        text.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .filter { it > 0 }

    fun baseTimestamp(anchorMillis: Long, hour: Int, minute: Int): Long {
        val zone = ZoneId.systemDefault()
        val anchor = Instant.ofEpochMilli(anchorMillis).atZone(zone)
        var candidate = anchor.toLocalDate().atTime(hour, minute).atZone(zone)
        if (!candidate.isAfter(anchor)) {
            candidate = candidate.plusDays(1)
        }
        return candidate.toInstant().toEpochMilli()
    }

    fun buildRevisions(
        topicId: Long,
        baseMillis: Long,
        intervals: List<Int>,
        nowMillis: Long,
    ): List<RevisionEntity> {
        val zone = ZoneId.systemDefault()
        val base = Instant.ofEpochMilli(baseMillis).atZone(zone)
        return intervals.mapIndexedNotNull { index, days ->
            val due = base.plusDays(days.toLong()).toInstant().toEpochMilli()
            if (due <= nowMillis) null else RevisionEntity(
                topicId = topicId,
                intervalIndex = index,
                intervalDays = days,
                dueAt = due,
                alertAt = due - ALERT_LEAD_MILLIS,
                status = "SCHEDULED",
            )
        }
    }

    fun previewTimestamps(nowMillis: Long, hour: Int, minute: Int, intervals: List<Int>): List<Long> {
        val zone = ZoneId.systemDefault()
        val base = Instant.ofEpochMilli(baseTimestamp(nowMillis, hour, minute)).atZone(zone)
        return intervals.map { base.plusDays(it.toLong()).toInstant().toEpochMilli() }
    }

    fun format(millis: Long): String =
        Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))

    fun isSameDay(a: Long, b: Long): Boolean {
        val zone = ZoneId.systemDefault()
        return Instant.ofEpochMilli(a).atZone(zone).toLocalDate() ==
            Instant.ofEpochMilli(b).atZone(zone).toLocalDate()
    }
}
