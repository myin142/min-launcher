package myin.phone.list;

import android.util.TypedValue;
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
    private int fontSize;

    public TextListAdapter(DiffUtil.ItemCallback<T> diffUtil) {
        this(diffUtil, -1);
    }

    public TextListAdapter(DiffUtil.ItemCallback<T> diffUtil, int size) {
        super(diffUtil);
        this.fontSize = size;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        if (fontSize != -1) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        }

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
