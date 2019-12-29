package myin.phone.list;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListTextAdapter<T, H extends ListTextAdapter.ListTextView> extends ListAdapter<T, H> {

    public ListTextAdapter(List<T> list) {
        this();
        submitList(list);
    }

    public ListTextAdapter() {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem.equals(newItem);
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
    }

    public static class ListTextView extends RecyclerView.ViewHolder {

        private TextView textView;

        public ListTextView(@NonNull TextView itemView) {
            super(itemView);
            textView = itemView;
        }
    }

}
