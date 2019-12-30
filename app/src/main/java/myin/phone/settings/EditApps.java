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
import myin.phone.R;
import myin.phone.SharedConst;
import myin.phone.apps.AppItem;
import myin.phone.apps.AppsList;
import myin.phone.list.ListItemAdapter;
import myin.phone.list.ReorderListItemCallback;
import myin.phone.shared.LoadAppsActivity;
import myin.phone.utils.IntentBuilder;
import myin.phone.utils.Stream;

import java.util.LinkedHashSet;
import java.util.List;

public class EditApps extends LoadAppsActivity {

    public static final String APPS_CHANGED = "editApps_changed";

    private static final int NEW_APP_REQUEST = 1;
    private static final int MAX_APPS = 7;

    private TextView addText;
    private RecyclerView editAppsList;
    private ListItemAdapter<AppItem> appsAdapter;
    private boolean changed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_edit_apps);

        addText = findViewById(R.id.test);
        addText.setOnClickListener(v -> openNewAppsList());

        editAppsList = findViewById(R.id.edit_apps_list);
        editAppsList.setLayoutManager(new LinearLayoutManager(this));
        editAppsList.setHasFixedSize(true);

        appsAdapter = new ListItemAdapter<>(appList);
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
        startActivityForResult(appsListIntent, NEW_APP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        String app = data.getStringExtra(AppsList.SELECTED_APP);
        switch (requestCode) {
            case NEW_APP_REQUEST:
                addNewApp(app);
                break;
        }
    }

    private void addNewApp(String appName) {
        AppItem app = new AppItem(getPackageManager(), appName);
        appsAdapter.addItem(app);
        updateList();
    }

    private void updateList() {
        updateAddButtonVisibility();
        changed = true;
    }

    private void updateAddButtonVisibility() {
        addText.setVisibility(appList.size() == MAX_APPS ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStop() {
        if (changed) {
            saveApps();

            changed = false;
            setResult(RESULT_OK, IntentBuilder.builder()
                    .put(APPS_CHANGED, true)
                    .build());
        }
        super.onStop();
    }

    private void saveApps() {
        SharedPreferences.Editor editor = preferences.edit();

        List<String> appPackages = Stream.map(appList, AppItem::getFullName);

        // Use LinkedHashSet to preserve order
        editor.putStringSet(SharedConst.PREF_APPS, new LinkedHashSet<>(appPackages));
        editor.apply();
    }
}
