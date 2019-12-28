package myin.phone.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;
import lombok.RequiredArgsConstructor;
import myin.phone.R;

import java.util.List;

@RequiredArgsConstructor
public class ListItemAdapter<T> extends RecyclerView.Adapter<ListItemAdapter.ListItemView> {

    private final List<T> list;
    private Consumer<T> itemClickListener;

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
                itemClickListener.accept(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onItemClickListener(Consumer<T> itemClickListener) {
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
