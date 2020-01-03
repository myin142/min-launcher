package myin.phone.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.annimon.stream.function.Consumer;
import lombok.Setter;
import myin.phone.R;

@Setter
public class TextListAdapter<T> extends ListAdapter<T, TextViewHolder> {

    private Consumer<T> onItemClickListener;

    public TextListAdapter(DiffUtil.ItemCallback<T> diffUtil) {
        super(diffUtil);
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new TextViewHolder(view, view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        T item = getItem(position);

        // Uses toString of Object on default
        // If required add custom function to map
        // to different value
        holder.setText(item.toString());
        holder.setOnTextClick(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.accept(item);
            }
        });
    }

}
