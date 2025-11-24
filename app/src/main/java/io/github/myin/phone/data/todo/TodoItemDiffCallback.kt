package io.github.myin.phone.data.todo;

import androidx.recyclerview.widget.DiffUtil;

class TodoItemDiffCallback() : DiffUtil.ItemCallback<TodoItem>() {

    override fun areItemsTheSame(p0: TodoItem, p1: TodoItem): Boolean {
        return p0.id == p1.id;
    }

    override fun areContentsTheSame(p0: TodoItem, p1: TodoItem): Boolean {
        return p0 == p1;
    }
}
