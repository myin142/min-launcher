package io.github.myin.phone.views.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import java.util.Collections;
import java.util.List;

public class AppsLoading extends Observable<List<ResolveInfo>> {
    private final Context context;

    private final Subject<String> reload = BehaviorSubject.create();

    private Runnable onLoadStart = () -> {};

    public AppsLoading(Context context) {
        this.context = context;
    }

    @Override
    protected void subscribeActual(Observer<? super List<ResolveInfo>> observer) {
        Disposable listDisposable = reload
                .map(x -> {
                    onLoadStart.run();
                    return x;
                })
                .observeOn(Schedulers.newThread())
                .map(x -> getAllApps())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer::onNext);

        observer.onSubscribe(listDisposable);
    }

    public void setOnLoadStart(Runnable onLoadStart) {
        this.onLoadStart = onLoadStart;
    }

    public void reload() {
        reload.onNext("");
    }

    private List<ResolveInfo> getAllApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(intent, 0);
        System.out.println("Found apps: " + apps.size());
        apps.sort(new ResolveInfo.DisplayNameComparator(getPackageManager()));

        return apps;
    }

    private PackageManager getPackageManager() {
        return context.getPackageManager();
    }

}
