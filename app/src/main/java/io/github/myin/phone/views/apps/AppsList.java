package io.github.myin.phone.views.apps;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.data.setting.AppSetting;
import io.github.myin.phone.data.setting.AppSettingRepository;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.utils.IntentBuilder;
import io.reactivex.disposables.Disposable;
import io.github.myin.phone.R;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AppsList extends AppCompatActivity {

    public static final String SELECTED_PACKAGE_APP = "appList_selectedPackage";
    public static final String SELECTED_CLASS_APP = "appList_selectedClass";

    private Disposable searchDisposable;
    private Disposable loadingDisposable;
    private AppListAdapter listAdapter;
    private AppsLoading appsLoading;

    @Inject
    AppSettingRepository appSettingRepository;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.searchDisposable.dispose();
        this.loadingDisposable.dispose();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);
        overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_bottom_out);

        listAdapter = new AppListAdapter(getPackageManager(), this::findAppSetting);
        listAdapter.setOnItemClickListener(this::closeWithAppResult);

        AppsListSearch appsListSearch = new AppsListSearch(getPackageManager());
        searchDisposable = appsListSearch.subscribe(this::setAppsList);

        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(appsListSearch);

        RecyclerView recyclerAppsList = findViewById(R.id.apps_list);
        recyclerAppsList.setHasFixedSize(true);
        recyclerAppsList.setLayoutManager(new LinearLayoutManager(this));
        recyclerAppsList.setAdapter(listAdapter);

        appsLoading = new AppsLoading(this);

        View progress = findViewById(R.id.progress_layout);
        appsLoading.setOnLoadStart(() -> {
            progress.setVisibility(View.VISIBLE);
            recyclerAppsList.setVisibility(View.GONE);
        });
        appsLoading.setOnLoadFinish(() -> {
            progress.setVisibility(View.GONE);
            recyclerAppsList.setVisibility(View.VISIBLE);
        });

        loadingDisposable = appsLoading.subscribe(appsListSearch::loadApps);
        appsLoading.reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        appsLoading.reload();
    }

    private void setAppsList(List<ResolveInfo> infoList) {
        List<ResolveInfo> filteredList = new ArrayList<>();
        for (ResolveInfo info: infoList) {
            AppSetting setting = findAppSetting(info);
            if (setting.isHidden()) {
                if (FeaturePreference.isFeatureEnabled(SharedConst.PREF_SHOW_HIDDEN_APPS)) {
                    filteredList.add(info);
                }
            } else {
                filteredList.add(info);
            }
        }

        listAdapter.submitList(filteredList);
    }

    private void reloadList() {
        setAppsList(listAdapter.getCurrentList());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getGroupId();
        ResolveInfo info = listAdapter.getCurrentList().get(position);

        switch (item.getItemId()) {
            case AppViewHolder.MENU_HIDE_ACTION:
                AppSetting app = findAppSetting(info);
                app.toggleHidden();
                appSettingRepository.insert(app);
                if (FeaturePreference.isFeatureEnabled(SharedConst.PREF_SHOW_HIDDEN_APPS)) {
                    listAdapter.notifyItemChanged(position);
                } else {
                    reloadList();
                }
                break;
            case AppViewHolder.MENU_UNINSTALL_ACTION:
                Intent uninstallIntent = new IntentBuilder(info).uninstall();
                startActivity(uninstallIntent);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private AppSetting findAppSetting(ResolveInfo info) {
        String pkg = info.activityInfo.packageName;
        String cls = info.activityInfo.name;

        List<AppSetting> apps = appSettingRepository.findByPackageAndClass(pkg, cls);

        if (apps.isEmpty()) {
            return new AppSetting(pkg, cls);
        } else {
            return apps.get(0);
        }
    }

    private void closeWithAppResult(ResolveInfo app) {
        Intent data = new Intent();
        data.putExtra(SELECTED_PACKAGE_APP, app.activityInfo.packageName);
        data.putExtra(SELECTED_CLASS_APP, app.activityInfo.name);

        setResult(RESULT_OK, data);
        finish();
    }

}
