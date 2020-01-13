package myin.phone.views.settings.apps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import myin.phone.R;
import myin.phone.list.OnListChangeListener;
import myin.phone.views.apps.AppsList;
import myin.phone.data.app.HomeApp;
import myin.phone.data.app.HomeAppRepository;
import myin.phone.list.NoScrollLinearLayout;
import myin.phone.views.SelectAppActivity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ManageAppsActivity extends SelectAppActivity implements OnListChangeListener<HomeApp> {

    private static final int REQ_NEW_APP = 1;
    private static final int REQ_EDIT_APP = 2;
    private static final int REQ_OPEN_APP = 3;
    private static final int MAX_APPS = 7;

    private TextView addText;
    private ManageAppsAdapter appsAdapter;
    private HomeApp editApp;

    @Inject
    HomeAppRepository homeAppRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_apps_edit);

        addText = findViewById(R.id.action_add);
        addText.setOnClickListener(v -> openNewAppsList());

        TextView openAppText = findViewById(R.id.action_open_app);
        openAppText.setOnClickListener(v -> openAppsList());

        homeAppRepository.getHomeAppsSorted().observe(this, list -> {
            appsAdapter.submitList(list);
            updateAddButtonVisibility();
        });

        RecyclerView editAppsList = findViewById(R.id.edit_apps_list);
        editAppsList.setLayoutManager(new NoScrollLinearLayout(this));
        editAppsList.setHasFixedSize(true);

        appsAdapter = new ManageAppsAdapter();
        appsAdapter.setOnListChange(this);
        appsAdapter.setOnItemClick(this::openEditAppsList);

        editAppsList.setAdapter(appsAdapter);

        ItemTouchHelper touchHelper = new ManageAppsTouchHelper(appsAdapter);
        touchHelper.attachToRecyclerView(editAppsList);
    }

    private void openNewAppsList() {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_NEW_APP);
    }

    private void openEditAppsList(HomeApp app) {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_EDIT_APP);
        editApp = app;
    }

    private void openAppsList() {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_OPEN_APP);
    }

    @Override
    protected void onAppSelected(int requestCode, HomeApp homeApp) {
        switch (requestCode) {
            case REQ_NEW_APP:
                appsAdapter.addItem(homeApp);
                break;
            case REQ_EDIT_APP:
                editApp.copyValuesFrom(homeApp);
                appsAdapter.updateItem(editApp);
                break;
            case REQ_OPEN_APP:
                startActivity(homeApp.getActivityIntent());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Reset edit index after every closed activity
        editApp = null;
    }

    private void updateAddButtonVisibility() {
        addText.setVisibility(appsAdapter.getItemCount() == MAX_APPS ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemAdded(HomeApp app) {
        homeAppRepository.insert(app);
    }

    @Override
    public void onItemDeleted(HomeApp app) {
        homeAppRepository.delete(app);
    }

    @Override
    public void syncApps() {
        List<HomeApp> apps = new ArrayList<>(appsAdapter.getCurrentList());
        appsAdapter.updateIndexes(apps);
        homeAppRepository.update(apps.toArray(new HomeApp[0]));
    }

}