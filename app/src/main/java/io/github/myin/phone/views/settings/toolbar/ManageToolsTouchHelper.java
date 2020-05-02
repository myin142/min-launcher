package io.github.myin.phone.views.settings.toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ManageToolsTouchHelper extends ItemTouchHelper {

    public ManageToolsTouchHelper(ManageToolsAdapter adapter) {
        super(new Callback(adapter));
    }

    public static class Callback extends SimpleCallback {

        private ManageToolsAdapter adapter;

        public Callback(ManageToolsAdapter adapter) {
            super(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.UP);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            adapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.UP) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
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
