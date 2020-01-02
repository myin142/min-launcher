package myin.phone.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import myin.phone.R;
import myin.phone.apps.AppItem;
import myin.phone.apps.AppsList;
import myin.phone.data.app.HomeApp;
import myin.phone.data.app.HomeAppRepository;
import myin.phone.shared.OpenAppsActivity;

import javax.inject.Inject;

public class ManageAppsActivity extends OpenAppsActivity implements OnListAdapterChange<HomeApp> {

    private static final int REQ_NEW_APP = 1;
    private static final int REQ_EDIT_APP = 2;
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
        setContentView(R.layout.settings_edit_apps);

        addText = findViewById(R.id.test);
        addText.setOnClickListener(v -> openNewAppsList());

        homeAppRepository.getHomeApps().observe(this, list -> {
            updateAddButtonVisibility();
        });

        RecyclerView editAppsList = findViewById(R.id.edit_apps_list);
        editAppsList.setLayoutManager(new LinearLayoutManager(this));
        editAppsList.setHasFixedSize(true);

        appsAdapter = new ManageAppsAdapter();
        appsAdapter.setOnListChange(this);
        appsAdapter.setOnItemClick(this::openEditAppsList);
        appsAdapter.submitList(homeAppRepository.getHomeApps().getValue());

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

    @Override
    protected void onAppSelected(int requestCode, AppItem appItem) {
        switch (requestCode) {
            case REQ_NEW_APP:
                appsAdapter.addItem(appItem.toHomeApp());
                break;
            case REQ_EDIT_APP:
                editApp.label = appItem.getLabel();
                editApp.packageName = appItem.getPackageName();
                editApp.className = appItem.getClassName();
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
    public void onItemAdded(HomeApp item) {
        homeAppRepository.insert(item);
    }

    @Override
    public void onItemDeleted(HomeApp item) {
        homeAppRepository.delete(item);
    }

    @Override
    public void onItemMoved(HomeApp target, HomeApp dest) {
        homeAppRepository.update(target, dest);
    }
}
