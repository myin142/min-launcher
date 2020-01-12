package myin.phone.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import myin.phone.data.BaseApp;

public class BaseAppDiffCallback extends DiffUtil.ItemCallback<BaseApp> {

    @Override
    public boolean areItemsTheSame(@NonNull BaseApp oldItem, @NonNull BaseApp newItem) {
        return oldItem.id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(@NonNull BaseApp oldItem, @NonNull BaseApp newItem) {
        return oldItem.packageName.equals(newItem.packageName) &&
                oldItem.className.equals(newItem.className);
    }

}
