package io.github.myin.phone.data.calendar

import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.room.util.newStringBuilder
import io.github.myin.phone.utils.FeaturePreference
import java.time.*

class CalendarService {

    fun readCalendar(context: Context): List<Calendar> {
        val cursor = context.contentResolver
            .query(
                CalendarContract.Calendars.CONTENT_URI,
                arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.NAME), null, null, null
            )

        return cursorToList(cursor) {
            Calendar(it.getLong(0), it.getString(1))
        }
    }

    fun readCalendarEvent(
        context: Context,
        fromTime: ZonedDateTime,
        duration: Duration = Duration.ofDays(30),
        take: Int = 10,
    ): List<CalendarEvent> {
        val calendar = FeaturePreference.getCalendarEnabled();
        if (calendar.isEmpty()) {
            return emptyList()
        }

        val endTime = fromTime.plus(duration);
        val selection =
            "(( ${CalendarContract.Events.DTSTART} >= ${
                fromTime.toInstant().toEpochMilli()
            }) AND (${CalendarContract.Events.DTSTART} <= ${
                endTime.toInstant().toEpochMilli()
            }) AND (${CalendarContract.Events.CALENDAR_ID} IN (${calendar.joinToString(",")})) AND ( deleted != 1 ))";

        val cursor = context.contentResolver
            .query(
                CalendarContract.Events.CONTENT_URI,
                arrayOf(CalendarContract.Events._ID, CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART),
                selection,
                null,
                "${CalendarContract.Events.DTSTART} ASC"
            )

        return cursorToList(cursor) {
            CalendarEvent(
                id = it.getLongOrNull(0) ?: -1,
                title = it.getStringOrNull(1) ?: "No title",
                date = getDate(it.getLongOrNull(2)) ?: LocalDateTime.now(),
            )
        }.take(take);
    }

    private fun <T> cursorToList(cursor: Cursor?, mapFn: (c: Cursor) -> T): List<T> {
        if (cursor == null || cursor.count == 0 || !cursor.moveToFirst()) {
            return emptyList()
        }

        val result = mutableListOf<T>()
        for (i in 1..cursor.count) {
            result.add(mapFn(cursor))
            cursor.moveToNext()
        }
        return result
    }

    private fun getDate(milliSeconds: Long?): LocalDateTime? {
        if (milliSeconds == null) {
            return null
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliSeconds), ZoneId.systemDefault())
    }
}
