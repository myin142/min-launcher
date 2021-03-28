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
import io.github.myin.phone.data.setting.AppSetting;
import io.github.myin.phone.data.setting.AppSettingRepository;
import io.reactivex.disposables.Disposable;
import io.github.myin.phone.R;
import io.github.myin.phone.list.TextListAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppsList extends AppCompatActivity {

    public static final String SELECTED_PACKAGE_APP = "appList_selectedPackage";
    public static final String SELECTED_CLASS_APP = "appList_selectedClass";

    private Disposable searchDisposable;
    private Disposable loadingDisposable;
    private TextListAdapter<ResolveInfo, AppViewHolder> listAdapter;

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

        listAdapter = new TextListAdapter<>(new ResolveInfoDiffCallback(), AppViewHolder::new);
        listAdapter.setDisplayFunction(resolve -> resolve.loadLabel(getPackageManager()).toString());
        listAdapter.setOnItemClickListener(this::closeWithAppResult);

        AppsListSearch appsListSearch = new AppsListSearch(getPackageManager());
        searchDisposable = appsListSearch.subscribe(this::setAppsList);

        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(appsListSearch);

        RecyclerView recyclerAppsList = findViewById(R.id.apps_list);
        recyclerAppsList.setHasFixedSize(true);
        recyclerAppsList.setLayoutManager(new LinearLayoutManager(this));
        recyclerAppsList.setAdapter(listAdapter);

        AppsLoading appsLoading = new AppsLoading(this);
        loadingDisposable = appsLoading.subscribe(apps -> {
            appsListSearch.loadedApps(apps);
            setAppsList(apps);
            findViewById(R.id.progress_layout).setVisibility(View.GONE);
            recyclerAppsList.setVisibility(View.VISIBLE);
        });
    }

    private void setAppsList(List<ResolveInfo> infoList) {
        List<ResolveInfo> filteredList = new ArrayList<>();
        for (ResolveInfo resolveInfo : infoList) {
            if (!findAppSetting(resolveInfo).isHidden()) {
                filteredList.add(resolveInfo);
            }
        }

        listAdapter.submitList(filteredList);
    }

    private void reloadList() {
        setAppsList(listAdapter.getCurrentList());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case AppViewHolder.MENU_HIDE_ACTION:
                ResolveInfo info = listAdapter.getCurrentList().get(item.getGroupId());
                AppSetting setting = findAppSetting(info);
                setting.toggleHidden();
                appSettingRepository.insert(setting);
                reloadList();
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
