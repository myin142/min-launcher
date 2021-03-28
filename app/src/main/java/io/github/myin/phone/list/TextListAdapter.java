package io.github.myin.phone.list;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import lombok.Setter;
import io.github.myin.phone.R;

@Setter
public class TextListAdapter<T, H extends TextViewHolder> extends ListAdapter<T, H> {

    private Function<T, String> displayFunction;
    private Consumer<T> onItemClickListener;
    private int fontSize;
    private BiFunction<View, TextView, H> constructor;

    public TextListAdapter(DiffUtil.ItemCallback<T> diffUtil, BiFunction<View, TextView, H> constructor) {
        this(diffUtil, constructor, -1);
    }

    public TextListAdapter(DiffUtil.ItemCallback<T> diffUtil, BiFunction<View, TextView, H> constructor, int size) {
        super(diffUtil);
        this.fontSize = size;
        this.constructor = constructor;
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        if (fontSize != -1) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        }

        return constructor.apply(view, view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        T item = getItem(position);

        String text = (this.displayFunction != null) ? this.displayFunction.apply(item) : item.toString();
        holder.setText(text);

        holder.setOnTextClick(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.accept(item);
            }
        });
    }

}
