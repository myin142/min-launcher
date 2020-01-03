package myin.phone.list;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class TextViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;

    public TextViewHolder(View view, TextView textView) {
        super(view);
        this.textView = textView;
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setOnTextClick(View.OnClickListener clickListener) {
        this.textView.setOnClickListener(clickListener);
    }
}
