package io.github.myin.phone.views.apps;

import android.content.pm.ResolveInfo;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ResolveInfoDiffCallback extends DiffUtil.ItemCallback<ResolveInfo> {

    @Override
    public boolean areItemsTheSame(@NonNull ResolveInfo oldItem, @NonNull ResolveInfo newItem) {
        return oldItem.activityInfo.packageName.equals(newItem.activityInfo.packageName) &&
                oldItem.activityInfo.name.equals(newItem.activityInfo.name);
    }

    @Override
    public boolean areContentsTheSame(@NonNull ResolveInfo oldItem, @NonNull ResolveInfo newItem) {
        return oldItem.activityInfo.packageName.equals(newItem.activityInfo.packageName) &&
                oldItem.activityInfo.name.equals(newItem.activityInfo.name);
    }

}
