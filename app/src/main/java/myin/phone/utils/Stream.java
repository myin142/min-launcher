package myin.phone.utils;

import androidx.arch.core.util.Function;

import java.util.ArrayList;
import java.util.List;

public class Stream {

    public static <T1, T2> List<T2> map(List<T1> list, Function<T1, T2> mapFn) {
        List<T2> newList = new ArrayList<>();
        for (T1 item : list) {
            newList.add(mapFn.apply(item));
        }
        return newList;
    }

}
