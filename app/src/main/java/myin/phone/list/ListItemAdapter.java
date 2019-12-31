package myin.phone.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;
import com.annimon.stream.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import myin.phone.R;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ListItemAdapter<T> extends RecyclerView.Adapter<ListItemAdapter.ListItemView> {

    private final List<T> list;
    private BiConsumer<T, Integer> itemClickListener;

    @NonNull
    @Override
    public ListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ListItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemView holder, int position) {
        TextView textView = holder.textView;
        T item = list.get(position);

        // Uses toString of Object on default
        // If required add custom function to map
        // to different value
        textView.setText(item.toString());

        textView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.accept(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onItemClickListener(BiConsumer<T, Integer> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void addItem(T item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(int position, T item) {
        list.set(position, item);
        notifyItemChanged(position);
    }

    public static class ListItemView extends RecyclerView.ViewHolder {

        private TextView textView;

        public ListItemView(@NonNull TextView itemView) {
            super(itemView);
            textView = itemView;
        }
    }
}
