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
import myin.phone.utils.Stream;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class Settings extends LoadAppsActivity {

    private static final Logger log = Logger.getLogger("Settings");
    private static final int NEW_APP_REQUEST = 1;
    private static final int MAX_APPS = 7;

    private TextView addAppView;
    private RecyclerView editAppList;
    private ListItemAdapter<AppItem> appsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        addAppView = findViewById(R.id.add_app);
        addAppView.setOnClickListener(v -> openNewAppsList());

        editAppList = findViewById(R.id.edit_apps_list);
        editAppList.setLayoutManager(new LinearLayoutManager(this));
        editAppList.setHasFixedSize(true);

        appsAdapter = new ListItemAdapter<>(appList);
        editAppList.setAdapter(appsAdapter);

        ItemTouchHelper.Callback callback = new ReorderListItemCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.END) {
                    appsAdapter.removeItem(viewHolder.getAdapterPosition());
                    updateList();
                }
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(editAppList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAddButtonVisibility();
    }

    private void updateList() {
        updateAddButtonVisibility();
        markAppsDirty();
    }

    private void updateAddButtonVisibility() {
        addAppView.setVisibility(appList.size() == MAX_APPS ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        log.info("Pause: " + appsHasChanged());
        if (appsHasChanged()) {
            saveApps();
        }
    }

    private void saveApps() {
        SharedPreferences.Editor editor = preferences.edit();

        List<String> appPackages = Stream.map(appList, AppItem::getFullName);
        editor.putStringSet(SharedConst.PREF_APPS, new HashSet<>(appPackages));
        System.out.println("Save: " + appPackages);

        editor.apply();
    }

    public void openNewAppsList() {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, NEW_APP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        log.info("Result for " + requestCode + ": " + resultCode);

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
        System.out.println("Add: " + appName);
        markAppsDirty();
    }

}
