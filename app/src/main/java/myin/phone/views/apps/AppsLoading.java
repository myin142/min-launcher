package myin.phone.views.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import myin.phone.data.BaseApp;
import myin.phone.data.app.HomeApp;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class AppsLoading extends Observable<List<ResolveInfo>> {
    private Context context;

    @Override
    protected void subscribeActual(Observer<? super List<ResolveInfo>> observer) {
        Disposable listDisposable = Observable.create((ObservableOnSubscribe<List<ResolveInfo>>) emitter -> {
            emitter.onNext(getAllAppsAsHomeApps());
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    observer.onNext(list);
                    observer.onComplete();
                });

        observer.onSubscribe(listDisposable);
    }

    private List<ResolveInfo> getAllAppsAsHomeApps() {
        return Stream.of(getAllApps())
//                .map(this::infoToHomeApp)
                .collect(Collectors.toList());
    }

    private HomeApp infoToHomeApp(ResolveInfo resolveInfo) {
        ActivityInfo info = resolveInfo.activityInfo;
        return new HomeApp(info.packageName, info.name, resolveInfo.loadLabel(getPackageManager()).toString());
    }

    private List<ResolveInfo> getAllApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(intent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(getPackageManager()));

        return apps;
    }

    private PackageManager getPackageManager() {
        return context.getPackageManager();
    }

}
