package io.github.myin.phone.list;

import java.util.List;

public interface OnListChangeListener<T> {
    void onItemAdded(T item);
    void onItemDeleted(T item);
    void syncItems(List<T> items);
}
