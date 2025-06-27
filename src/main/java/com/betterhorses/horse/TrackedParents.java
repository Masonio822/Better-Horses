package com.betterhorses.horse;

public interface TrackedParents<T> {
    void setParents(T horse1, T horse2);

    T[] getParents();
}
