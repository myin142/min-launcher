package io.github.myin.phone.views.apps;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.myin.phone.R;
import io.github.myin.phone.data.setting.AppSetting;

public class AppListAdapter extends ListAdapter<ResolveInfo, AppViewHolder> {

    private final PackageManager packageManager;
    private final Function<ResolveInfo, AppSetting> infoSettingFn;

    private Consumer<ResolveInfo> onItemClickListener;

    public AppListAdapter(PackageManager packageManager, Function<ResolveInfo, AppSetting> infoSettingFn) {
        super(new ResolveInfoDiffCallback());
        this.packageManager = packageManager;
        this.infoSettingFn = infoSettingFn;
    }

    public void setOnItemClickListener(Consumer<ResolveInfo> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        ResolveInfo item = getItem(position);
        AppSetting setting = infoSettingFn.apply(item);

        final var text = Optional.ofNullable(setting.getCustomName())
                .filter(x -> !x.isBlank())
                .orElseGet(() -> item.loadLabel(packageManager).toString());
        holder.setText(text);

        holder.setAppSetting(setting);
        holder.getTextView().setPaintFlags(setting.isHidden() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);

        holder.setOnTextClick(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.accept(item);
            }
        });
    }
}
