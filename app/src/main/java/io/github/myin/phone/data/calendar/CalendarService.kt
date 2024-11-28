package io.github.myin.phone.data.calendar

import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
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
        duration: Duration = Duration.ofDays(30)
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
                arrayOf(CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART), selection, null, null
            )

        return cursorToList(cursor) {
            CalendarEvent(
                id = 1,
                title = it.getString(0),
                date = getDate(it.getString(1).toLong()),
            )
        }
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

    private fun getDate(milliSeconds: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliSeconds), ZoneId.systemDefault())
    }
}
