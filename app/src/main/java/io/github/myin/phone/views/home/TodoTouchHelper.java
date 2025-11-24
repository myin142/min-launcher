package io.github.myin.phone.views.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import io.github.myin.phone.data.todo.TodoItem;
import io.github.myin.phone.data.todo.TodoItemRepository;
import io.github.myin.phone.list.TextListAdapter;

public class TodoTouchHelper extends ItemTouchHelper {

    public TodoTouchHelper(TextListAdapter<TodoItem> adapter, TodoItemRepository repository) {
        super(new Callback(adapter, repository));
    }

    public static class Callback extends SimpleCallback {

        private final TextListAdapter<TodoItem> adapter;
        private final TodoItemRepository repository;

        public Callback(TextListAdapter<TodoItem> adapter, TodoItemRepository repository) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.adapter = adapter;
            this.repository = repository;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // No move support for todos
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && pos >= 0 && pos < adapter.getCurrentList().size()) {
                TodoItem item = adapter.getCurrentList().get(pos);
                // delete from DB
                repository.delete(item);
            }
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder != null) {
                viewHolder.itemView.setAlpha(0.5f);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1);
        }

    }
}
