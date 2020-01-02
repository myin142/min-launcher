package myin.phone.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.annimon.stream.function.BiConsumer;
import myin.phone.R;

import java.util.List;

public class ListItemAdapter<T> extends ListAdapter<T, ListItemAdapter.ListItemView> /*RecyclerView.Adapter<ListItemAdapter.ListItemView>*/ {

    private BiConsumer<T, Integer> itemClickListener;

    public ListItemAdapter(List<T> initialList) {
        this();
        submitList(initialList);
    }

    public ListItemAdapter() {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

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
        T item = getItem(position);

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

    public void onItemClickListener(BiConsumer<T, Integer> itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    public static class ListItemView extends RecyclerView.ViewHolder {

        private TextView textView;

        public ListItemView(@NonNull TextView itemView) {
            super(itemView);
            textView = itemView;
        }
    }
}
