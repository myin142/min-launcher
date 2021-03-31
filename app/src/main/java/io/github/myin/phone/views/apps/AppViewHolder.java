package io.github.myin.phone.views.apps;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;
import com.annimon.stream.Optional;
import io.github.myin.phone.R;
import io.github.myin.phone.data.setting.AppSetting;
import io.github.myin.phone.list.TextViewHolder;

public class AppViewHolder extends TextViewHolder implements View.OnCreateContextMenuListener {

    public static final int MENU_HIDE_ACTION = 0;
    public static final int MENU_UNINSTALL_ACTION = 1;

    private AppSetting appSetting;

    public AppViewHolder(TextView textView) {
        super(textView);
        textView.setOnCreateContextMenuListener(this);
        textView.setLongClickable(true);
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

        menu.add(getAdapterPosition(), MENU_HIDE_ACTION, 0, hideTxt);
        menu.add(getAdapterPosition(), MENU_UNINSTALL_ACTION, 0, R.string.app_uninstall);
    }

}
