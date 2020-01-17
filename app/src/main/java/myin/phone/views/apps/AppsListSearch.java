package myin.phone.views.apps;

import android.text.Editable;
import android.text.TextWatcher;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import myin.phone.data.app.HomeApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppsListSearch extends Observable<List<HomeApp>> implements TextWatcher {

    private List<HomeApp> appsList = new ArrayList<>();
    private Subject<String> changed = ReplaySubject.create();

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

    public void loadedApps(List<HomeApp> apps) {
        appsList.addAll(apps);
    }

    @Override
    protected void subscribeActual(Observer<? super List<HomeApp>> observer) {
        Disposable searchDisposable = changed.debounce(100, TimeUnit.MILLISECONDS)
                .map(this::filteredAppsList)
                .subscribe(observer::onNext);

        observer.onSubscribe(searchDisposable);
    }

    private List<HomeApp> filteredAppsList(String name) {
        return Stream.of(appsList)
                .filter(app -> app.label.toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

}
