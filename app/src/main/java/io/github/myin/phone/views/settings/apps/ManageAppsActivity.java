package io.github.myin.phone.views.settings.apps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.github.myin.phone.R;
import io.github.myin.phone.data.setting.AppSettingRepository;
import io.github.myin.phone.list.OnListChangeListener;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.views.apps.AppsList;
import io.github.myin.phone.data.app.HomeApp;
import io.github.myin.phone.data.app.HomeAppRepository;
import io.github.myin.phone.list.NoScrollLinearLayout;
import io.github.myin.phone.views.SelectAppActivity;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class ManageAppsActivity extends SelectAppActivity implements OnListChangeListener<HomeApp> {

    private static final int REQ_NEW_APP = 1;
    private static final int REQ_EDIT_APP = 2;

    private TextView addText;
    private ManageAppsAdapter appsAdapter;
    private HomeApp editApp;

    @Inject
    HomeAppRepository homeAppRepository;
    @Inject
    AppSettingRepository appSettingRepository;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_apps_edit);

        findViewById(R.id.root).setLayoutDirection(FeaturePreference.getLayoutDirection().getValue());

        addText = findViewById(R.id.action_add);
        addText.setOnClickListener(v -> openNewAppsList());

        homeAppRepository.getHomeAppsSorted().observe(this, list -> appsAdapter.submitList(list));

        RecyclerView editAppsList = findViewById(R.id.edit_apps_list);
        editAppsList.setLayoutManager(new NoScrollLinearLayout(this));
        editAppsList.setHasFixedSize(true);

        appsAdapter = new ManageAppsAdapter();
        appsAdapter.setDisplayFn(app -> {
            final var appSetting = appSettingRepository.getOneByPackageAndClass(app.packageName, app.className);
            return Optional.ofNullable(appSetting.getCustomName())
                    .filter(x -> !x.isBlank())
                    .orElse(app.toString());
        });
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

    @Override
    protected void onAppSelected(int requestCode, ResolveInfo info) {
        HomeApp homeApp = resolveToHomeApp(info);
        switch (requestCode) {
            case REQ_NEW_APP:
                appsAdapter.addItem(homeApp);
                break;
            case REQ_EDIT_APP:
                editApp.copyValuesFrom(homeApp);
                appsAdapter.updateItem(editApp);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Reset edit index after every closed activity
        editApp = null;
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
    public void syncItems(List<HomeApp> apps) {
        homeAppRepository.update(apps.toArray(new HomeApp[0]));
    }

}
