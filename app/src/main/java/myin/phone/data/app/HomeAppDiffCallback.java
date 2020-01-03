package myin.phone.data.app;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class HomeAppDiffCallback extends DiffUtil.ItemCallback<HomeApp> {

    @Override
    public boolean areItemsTheSame(@NonNull HomeApp oldItem, @NonNull HomeApp newItem) {
        return oldItem.id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(@NonNull HomeApp oldItem, @NonNull HomeApp newItem) {
        return oldItem.packageName.equals(newItem.packageName) &&
                oldItem.className.equals(newItem.className) &&
                oldItem.label.equals(newItem.label);
    }

}
