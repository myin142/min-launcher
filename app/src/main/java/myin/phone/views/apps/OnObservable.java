package myin.phone.views.apps;

import io.reactivex.Observable;

public interface OnObservable<T> {
    void observe(Observable<T> observable);
}
