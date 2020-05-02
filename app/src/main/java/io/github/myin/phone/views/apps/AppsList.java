package io.github.myin.phone.views.apps;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;
import io.github.myin.phone.R;
import io.github.myin.phone.list.TextListAdapter;

public class AppsList extends AppCompatActivity {

    public static final String SELECTED_PACKAGE_APP = "appList_selectedPackage";
    public static final String SELECTED_CLASS_APP = "appList_selectedClass";

    private Disposable searchDisposable;
    private Disposable loadingDisposable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.searchDisposable.dispose();
        this.loadingDisposable.dispose();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);
        overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_bottom_out);

        TextListAdapter<ResolveInfo> listAdapter = new TextListAdapter<>(new ResolveInfoDiffCallback());
        listAdapter.setDisplayFunction(resolve -> resolve.loadLabel(getPackageManager()).toString());
        listAdapter.setOnItemClickListener(this::closeWithAppResult);

        AppsListSearch appsListSearch = new AppsListSearch(getPackageManager());
        searchDisposable = appsListSearch.subscribe(listAdapter::submitList);

        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(appsListSearch);

        RecyclerView recyclerAppsList = findViewById(R.id.apps_list);
        recyclerAppsList.setHasFixedSize(true);
        recyclerAppsList.setLayoutManager(new LinearLayoutManager(this));
        recyclerAppsList.setAdapter(listAdapter);

        AppsLoading appsLoading = new AppsLoading(this);
        loadingDisposable = appsLoading.subscribe(apps -> {
            appsListSearch.loadedApps(apps);
            listAdapter.submitList(apps);
            findViewById(R.id.progress_layout).setVisibility(View.GONE);
            recyclerAppsList.setVisibility(View.VISIBLE);
        });
    }

    private void closeWithAppResult(ResolveInfo app) {
        Intent data = new Intent();
        data.putExtra(SELECTED_PACKAGE_APP, app.activityInfo.packageName);
        data.putExtra(SELECTED_CLASS_APP, app.activityInfo.name);

        setResult(RESULT_OK, data);
        finish();
    }

}
