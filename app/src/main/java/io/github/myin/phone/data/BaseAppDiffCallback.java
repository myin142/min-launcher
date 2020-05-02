package io.github.myin.phone.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class BaseAppDiffCallback<T extends BaseApp> extends DiffUtil.ItemCallback<T> {

    @Override
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.packageName.equals(newItem.packageName) &&
                oldItem.className.equals(newItem.className);
    }

}
