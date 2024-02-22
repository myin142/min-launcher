package io.github.myin.phone.views.apps;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Editable;
import android.text.TextWatcher;

import io.github.myin.phone.data.setting.AppSetting;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppsListSearch extends Observable<List<ResolveInfo>> implements TextWatcher {

    private final PackageManager packageManager;

    private final Function<ResolveInfo, AppSetting> infoAppSettingFunction;

    private final BehaviorSubject<String> changed = BehaviorSubject.create();
    private List<ResolveInfo> appsList = new ArrayList<>();

    private Runnable onFinish = () -> {};

    public AppsListSearch(PackageManager packageManager, Function<ResolveInfo, AppSetting> infoSettingFn) {
        this.packageManager = packageManager;
        this.infoAppSettingFunction = infoSettingFn;
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
                .filter(app -> {
                    final var appSetting = this.infoAppSettingFunction.apply(app);
                    final var originalName = app.loadLabel(packageManager).toString();

                    return Stream.of(originalName, appSetting.getCustomName())
                            .filter(x -> x != null && !x.isBlank())
                            .map(String::toLowerCase)
                            .anyMatch(x -> x.contains(name.toLowerCase()));
                })
                .collect(Collectors.toList());
    }

}
