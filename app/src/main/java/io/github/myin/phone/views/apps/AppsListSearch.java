package io.github.myin.phone.views.apps;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Editable;
import android.text.TextWatcher;
import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AppsListSearch extends Observable<List<ResolveInfo>> implements TextWatcher {

    private final PackageManager packageManager;

    private final BehaviorSubject<String> changed = BehaviorSubject.create();
    private List<ResolveInfo> appsList = new ArrayList<>();

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
        Disposable searchDisposable = changed.debounce(100, TimeUnit.MILLISECONDS)
                .map(this::filteredAppsList)
                .subscribe(observer::onNext);

        observer.onSubscribe(searchDisposable);
    }

    private List<ResolveInfo> filteredAppsList(String name) {
        return Stream.of(appsList)
                .filter(app -> app.loadLabel(packageManager).toString().toLowerCase().contains(name.toLowerCase()))
//                .filter(app -> app.label.toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

}
