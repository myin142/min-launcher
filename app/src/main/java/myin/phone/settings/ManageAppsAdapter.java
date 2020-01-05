package myin.phone.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import lombok.Setter;
import myin.phone.R;
import myin.phone.data.app.HomeApp;
import myin.phone.data.app.HomeAppDiffCallback;
import myin.phone.list.TextViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Setter
public class ManageAppsAdapter extends ListAdapter<HomeApp, ManageAppsAdapter.ManageAppView> {

    private ManageAppsChangeListener onAppChange;
    private Consumer<HomeApp> onItemClick;

    public ManageAppsAdapter() {
        super(new HomeAppDiffCallback());
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
        holder.setOnTextClick(v -> {
            if (onItemClick != null) {
                onItemClick.accept(app);
            }
        });
    }

    public void addItem(HomeApp app) {
        updateCurrentList(list -> {
            list.add(app);
            updateIndexes(list);

            if (onAppChange != null) {
                onAppChange.onItemAdded(app);
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
            HomeApp app = list.remove(position);
            updateIndexes(list);

            if (onAppChange != null) {
                onAppChange.onItemDeleted(app);
            }
        });
    }

    public void updateItem(HomeApp homeApp) {
        updateCurrentList(list -> {
            int index = list.indexOf(homeApp);
            if (index != -1) {
                list.set(index, homeApp);
                notifyItemChanged(index);
            }
        });
    }

    private void updateCurrentList(Consumer<List<HomeApp>> listConsumer) {
        // getCurrentList() return read-only list
        List<HomeApp> current = new ArrayList<>(getCurrentList());
        listConsumer.accept(current);

        submitList(current, () -> {
            if (onAppChange != null) {
                onAppChange.syncApps();
            }
        });
    }

    public void updateIndexes(List<HomeApp> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).index = i;
        }
    }

    public static class ManageAppView extends TextViewHolder {
        public ManageAppView(View view) {
            super(view, view.findViewById(R.id.text));
        }
    }
}
