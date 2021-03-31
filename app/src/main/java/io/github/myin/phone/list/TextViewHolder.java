package io.github.myin.phone.list;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class TextViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;

    public TextViewHolder(TextView textView) {
        this(textView, textView);
    }

    public TextViewHolder(View view, TextView textView) {
        super(view);
        this.textView = textView;
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public TextView getTextView() {
        return textView;
    }

    public void setOnTextClick(View.OnClickListener clickListener) {
        this.textView.setOnClickListener(clickListener);
    }

}
