package io.github.myin.phone.list;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.function.Consumer;
import java.util.function.Function;

import io.github.myin.phone.R;
import lombok.Setter;

public class TextListAdapter<T> extends ListAdapter<T, TextViewHolder> {

    private Function<T, String> displayFunction;
    private Consumer<T> onItemClickListener;
    private final int fontSize;

    public TextListAdapter(DiffUtil.ItemCallback<T> diffUtil, int size) {
        super(diffUtil);
        this.fontSize = size;
    }

    public void setDisplayFunction(Function<T, String> displayFunction) {
        this.displayFunction = displayFunction;
    }

    public void setOnItemClickListener(Consumer<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

        String text = (this.displayFunction != null) ? this.displayFunction.apply(item) : item.toString();
        holder.setText(text);

        holder.setOnTextClick(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.accept(item);
            }
        });
    }

}
