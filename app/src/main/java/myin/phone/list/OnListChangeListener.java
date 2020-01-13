package myin.phone.list;

public interface OnListChangeListener<T> {
    void onItemAdded(T app);
    void onItemDeleted(T app);
    void syncApps();
}
