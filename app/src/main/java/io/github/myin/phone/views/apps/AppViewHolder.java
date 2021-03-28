package io.github.myin.phone.views.apps;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;
import io.github.myin.phone.list.TextViewHolder;

public class AppViewHolder extends TextViewHolder implements View.OnCreateContextMenuListener {

    public static final int MENU_HIDE_ACTION = 0;

    public AppViewHolder(View view, TextView textView) {
        super(view, textView);
        view.setOnCreateContextMenuListener(this);
        view.setLongClickable(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(getAdapterPosition(), MENU_HIDE_ACTION, 0, "Hide");
    }

}
