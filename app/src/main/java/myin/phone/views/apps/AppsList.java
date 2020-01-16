package myin.phone.views.apps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;
import myin.phone.R;
import myin.phone.data.app.HomeApp;
import myin.phone.data.BaseAppDiffCallback;
import myin.phone.list.TextListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppsList extends AppCompatActivity implements OnObservable<String> {

    public static final String SELECTED_PACKAGE_APP = "appList_selectedPackage";
    public static final String SELECTED_CLASS_APP  = "appList_selectedClass";
    public static final String SELECTED_LABEL_APP = "appList_selectedLabel";

    private TextListAdapter<HomeApp> listAdapter;
    private List<HomeApp> allApps;
    private Disposable searchDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);

        allApps = getAllAppsAsHomeApps();

        EditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new SearchInputChange(this));

        RecyclerView recyclerAppsList = findViewById(R.id.apps_list);
        recyclerAppsList.setHasFixedSize(true);
        recyclerAppsList.setLayoutManager(new LinearLayoutManager(this));

        listAdapter = new TextListAdapter<>(new BaseAppDiffCallback<>());
        listAdapter.setOnItemClickListener(appItem -> {
            Intent data = new Intent();
            data.putExtra(SELECTED_PACKAGE_APP, appItem.packageName);
            data.putExtra(SELECTED_CLASS_APP, appItem.className);
            data.putExtra(SELECTED_LABEL_APP, appItem.label);

            setResult(RESULT_OK, data);
            finish();
        });
        listAdapter.submitList(allApps);

        recyclerAppsList.setAdapter(listAdapter);
    }

    private List<HomeApp> getAllAppsAsHomeApps() {
        return Stream.of(getAllApps())
                .map(this::infoToHomeApp)
                .collect(Collectors.toList());
    }

    private HomeApp infoToHomeApp(ResolveInfo resolveInfo) {
        ActivityInfo info = resolveInfo.activityInfo;
        return new HomeApp(info.packageName, info.name, resolveInfo.loadLabel(getPackageManager()).toString());
    }

    private List<ResolveInfo> getAllApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));

        return apps;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (searchDisposable != null) {
            searchDisposable.dispose();
        }
    }

    @Override
    public void observe(Observable<String> observable) {
        searchDisposable = observable.debounce(100, TimeUnit.MILLISECONDS).subscribe(name -> {
            listAdapter.submitList(filteredAppsList(name));
        });
    }

    private List<HomeApp> filteredAppsList(String name) {
        return Stream.of(allApps)
                .filter(app -> app.label.toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
