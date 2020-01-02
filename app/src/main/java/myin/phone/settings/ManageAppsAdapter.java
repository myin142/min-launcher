package myin.phone.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.annimon.stream.function.Consumer;
import lombok.Setter;
import myin.phone.R;
import myin.phone.data.app.HomeApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
public class ManageAppsAdapter extends ListAdapter<HomeApp, ManageAppsAdapter.ManageAppView> {

    private OnListAdapterChange<HomeApp> onListChange;
    private OnListItemClick<HomeApp> onItemClick;

    protected ManageAppsAdapter() {
        super(new DiffUtil.ItemCallback<HomeApp>() {
            @Override
            public boolean areItemsTheSame(@NonNull HomeApp oldItem, @NonNull HomeApp newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull HomeApp oldItem, @NonNull HomeApp newItem) {
                return oldItem.packageName.equals(newItem.packageName) &&
                        oldItem.className.equals(newItem.className);
            }
        });
    }

    @NonNull
    @Override
    public ManageAppView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_app_item, parent, false);

        return new ManageAppView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageAppView holder, int position) {
        HomeApp app = getItem(position);
        holder.setText(app.label);
        holder.setOnClick(v -> onItemClick.onClick(getItem(position)));
    }

    public void addItem(HomeApp app) {
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

            updateIndexes(list);

            // Invert position since they already have changed
            if (onListChange != null) {
                onListChange.onItemMoved(list.get(toPosition), list.get(fromPosition));
            }
        });
    }

    public void deleteItem(int position) {
        updateCurrentList(list -> {
            HomeApp app = list.remove(position);
            updateIndexes(list);

            if (onListChange != null) {
                onListChange.onItemDeleted(app);
            }
        });
    }

    private void updateCurrentList(Consumer<List<HomeApp>> listConsumer) {
        // getCurrentList() return read-only list
        List<HomeApp> current = new ArrayList<>();
        Collections.copy(current, getCurrentList());

        listConsumer.accept(current);

        // Moved updateIndexes() inside each function
        // So the listener has the up-to-date indexes
        submitList(current);
    }

    private void updateIndexes(List<HomeApp> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).index = i;
        }
    }

    public static class ManageAppView extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

        public ManageAppView(View view) {
            super(view);
            this.view = view;
            this.textView = view.findViewById(R.id.text);
        }

        public void setText(String text) {
            this.textView.setText(text);
        }

        public void setOnClick(View.OnClickListener clickListener) {
            this.textView.setOnClickListener(clickListener);
        }
    }
}
