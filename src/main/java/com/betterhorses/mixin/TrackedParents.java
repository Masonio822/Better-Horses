package com.betterhorses.mixin;

import java.util.List;

public interface TrackedParents<T> {
    void addParents(T horse1, T horse2);

    List<T[]> getHistory();

    T[] getParents();
}
