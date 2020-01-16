package myin.phone.views.apps;

import android.text.Editable;
import android.text.TextWatcher;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class SearchInputChange implements TextWatcher {

    private Subject<String> changed = ReplaySubject.create();

    public SearchInputChange(OnObservable<String> onObservable) {
        onObservable.observe(changed);
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

}
