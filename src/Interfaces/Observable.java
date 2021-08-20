package Interfaces;

public interface Observable<T> {
    public void addObserver(Observer<T> channel);

    public void removeObserver(Observer<T> channel);

    public void update();
}
