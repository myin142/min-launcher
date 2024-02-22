package io.github.myin.phone.views.apps;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.Optional;

import io.github.myin.phone.R;
import io.github.myin.phone.data.setting.AppSetting;
import io.github.myin.phone.list.TextViewHolder;

public class AppViewHolder extends TextViewHolder implements View.OnCreateContextMenuListener {

    public static final int MENU_HIDE_ACTION = 0;
    public static final int MENU_UNINSTALL_ACTION = 1;
    public static final int MENU_RENAME_ACTION = 2;

    private AppSetting appSetting;

    public AppViewHolder(TextView textView) {
        super(textView);
        textView.setOnCreateContextMenuListener(this);
        textView.setLongClickable(true);
        textView.setOnLongClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            return false;
        });
    }

    public void setAppSetting(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    private boolean isHidden() {
        return Optional.ofNullable(appSetting)
                .map(AppSetting::isHidden)
                .orElse(false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        int hideTxt = isHidden() ? R.string.app_show : R.string.app_hide;

        menu.add(getAbsoluteAdapterPosition(), MENU_HIDE_ACTION, 0, hideTxt);
        menu.add(getAbsoluteAdapterPosition(), MENU_UNINSTALL_ACTION, 0, R.string.app_uninstall);
        menu.add(getAbsoluteAdapterPosition(), MENU_RENAME_ACTION, 0, R.string.app_rename);
    }

}
