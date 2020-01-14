package myin.phone.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.annimon.stream.function.Consumer;
import lombok.Setter;
import myin.phone.list.OnListChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
public abstract class BaseAppListAdapter<T extends BaseApp, V extends RecyclerView.ViewHolder> extends ListAdapter<T, V> {

    protected OnListChangeListener<T> onListChange;
    protected Consumer<T> onItemClick;

    protected BaseAppListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }


    public void addItem(T app) {
        updateCurrentList(list -> {
            list.add(app);
            updateIndexes(list);

            if (onListChange != null) {
                onListChange.onItemAdded(app);
            }
        });
    }

    public void moveItem(int fromPosition, int toPosition) {
        updateCurrentList(list -> {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(list, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(list, i, i - 1);
                }
            }
        });
    }

    public void deleteItem(int position) {
        updateCurrentList(list -> {
            T app = list.remove(position);
            updateIndexes(list);

            if (onListChange != null) {
                onListChange.onItemDeleted(app);
            }
        });
    }

    public void updateItem(T homeApp) {
        updateCurrentList(list -> {
            int index = list.indexOf(homeApp);
            if (index != -1) {
                list.set(index, homeApp);
                notifyItemChanged(index);
            }
        });
    }

    private void updateCurrentList(Consumer<List<T>> listConsumer) {
        // getCurrentList() return read-only list
        List<T> current = new ArrayList<>(getCurrentList());
        listConsumer.accept(current);

        submitList(current, () -> {
            if (onListChange != null) {
                List<T> items = new ArrayList<>(getCurrentList());
                updateIndexes(items);
                onListChange.syncItems(items);
            }
        });
    }

    private void updateIndexes(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).index = i;
        }
    }

}
