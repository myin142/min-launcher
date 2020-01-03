package myin.phone.settings;

public interface OnListAdapterChange<T> {
    void onItemAdded(T item);
    void onItemDeleted(T item);
    void onItemMoved(T target, T dest);
    void onItemUpdated(T item);
}
