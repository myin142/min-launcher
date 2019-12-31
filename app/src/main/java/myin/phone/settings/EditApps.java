package myin.phone.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import myin.phone.R;
import myin.phone.SharedConst;
import myin.phone.apps.AppItem;
import myin.phone.apps.AppsList;
import myin.phone.list.ListItemAdapter;
import myin.phone.list.ReorderListItemCallback;
import myin.phone.shared.LoadAppsActivity;
import myin.phone.utils.IntentBuilder;

public class EditApps extends LoadAppsActivity {

    public static final String APPS_CHANGED = "editApps_changed";

    private static final int REQ_NEW_APP = 1;
    private static final int REQ_EDIT_APP = 2;
    private static final int MAX_APPS = 7;

    private TextView addText;
    private ListItemAdapter<AppItem> appsAdapter;
    private boolean changed = false;
    private int editIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_edit_apps);

        addText = findViewById(R.id.test);
        addText.setOnClickListener(v -> openNewAppsList());

        RecyclerView editAppsList = findViewById(R.id.edit_apps_list);
        editAppsList.setLayoutManager(new LinearLayoutManager(this));
        editAppsList.setHasFixedSize(true);

        appsAdapter = new ListItemAdapter<>(appList);
        appsAdapter.onItemClickListener((appItem , p) -> openEditAppsList(p));
        editAppsList.setAdapter(appsAdapter);

        ItemTouchHelper.Callback callback = new ReorderListItemCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.END) {
                    appsAdapter.removeItem(viewHolder.getAdapterPosition());
                    updateList();
                }
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                updateList();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(editAppsList);
    }

    private void openNewAppsList() {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_NEW_APP);
    }

    private void openEditAppsList(int position) {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_EDIT_APP);
        editIndex = position;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            AppItem app = new AppItem(getPackageManager(), data.getStringExtra(AppsList.SELECTED_APP));
            switch (requestCode) {
                case REQ_NEW_APP:
                    appsAdapter.addItem(app);
                    break;
                case REQ_EDIT_APP:
                    appsAdapter.updateItem(editIndex, app);
                    break;
            }

            updateList();
        }

        // Reset edit index after every closed activity
        editIndex = -1;
    }

    private void updateList() {
        updateAddButtonVisibility();
        if (!changed) {
            changed = true;

            setResult(RESULT_OK, IntentBuilder.builder()
                    .put(APPS_CHANGED, true)
                    .build());
        }
    }

    private void updateAddButtonVisibility() {
        addText.setVisibility(appList.size() == MAX_APPS ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (changed) {
            saveApps();
        }
    }

    private void saveApps() {
        SharedPreferences.Editor editor = preferences.edit();

        // Use String because StringSet cannot be reordered
        String apps = Stream.of(appList).map(AppItem::getFullName).collect(Collectors.joining(SharedConst.PREF_APPS_DELIM));
        editor.putString(SharedConst.PREF_APPS, apps);

        editor.apply();
    }
}
