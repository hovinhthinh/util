package util;

public class Triple<F, S, T> {
    public F first;
    public S second;
    public T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Triple() {
        first = null;
        second = null;
        third = null;
    }
}