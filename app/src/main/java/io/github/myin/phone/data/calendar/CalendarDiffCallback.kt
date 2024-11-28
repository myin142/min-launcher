package io.github.myin.phone.data.calendar;

import androidx.recyclerview.widget.DiffUtil;

class CalendarDiffCallback() : DiffUtil.ItemCallback<CalendarEvent>() {

    override fun areItemsTheSame(p0: CalendarEvent, p1: CalendarEvent): Boolean {
        return p0.id == p1.id;
    }

    override fun areContentsTheSame(p0: CalendarEvent, p1: CalendarEvent): Boolean {
        return p0 == p1;
    }
}
