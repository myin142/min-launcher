package io.github.myin.phone.views.apps;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Editable;
import android.text.TextWatcher;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AppsListSearch extends Observable<List<ResolveInfo>> implements TextWatcher {

    private final PackageManager packageManager;

    private final BehaviorSubject<String> changed = BehaviorSubject.create();
    private List<ResolveInfo> appsList = new ArrayList<>();

    private Runnable onFinish = () -> {};

    public AppsListSearch(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public void setOnFinish(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        changed.onNext(s.toString());
    }

    public void loadApps(List<ResolveInfo> apps) {
        appsList = apps;
        String value = Optional.ofNullable(changed.getValue()).orElse("");
        changed.onNext(value);
    }

    @Override
    protected void subscribeActual(Observer<? super List<ResolveInfo>> observer) {
        Disposable searchDisposable = changed.debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                .map(this::filteredAppsList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    observer.onNext(x);
                    onFinish.run();
                });

        observer.onSubscribe(searchDisposable);
    }

    private List<ResolveInfo> filteredAppsList(String name) {
        return appsList.stream()
                .filter(app -> app.loadLabel(packageManager).toString().toLowerCase().contains(name.toLowerCase()))
//                .filter(app -> app.label.toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

}
