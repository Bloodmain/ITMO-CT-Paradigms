package queue;


/*
    Model: a[1]..a[n]
    Inv: n >= 0 && forall i=1..n: a[i] != null

    Let immutable(n): for i=1..n: a'[i] == a[i]

    Pre: element != null
    Post: n' = n + 1 && a[n'] == element && immutable(n)
        enqueue(element)

    Pre: n > 0
    Post: R == a[n] && n' == n && immutable(n)
        element()

    Pre: n > 0
    Post: R == a[1] && n' == n - 1 && forall i=1..n': a'[i] == a[i + 1]
        dequeue()

    Pre: true
    Post: R == n && n' == n && immutable(n)
        size()

    Pre: true
    Post: R == (n == 0) && n' == n && immutable(n)
        isEmpty()

    Pre: true
    Post: n' == 0
        clear()

    Pre: n > 0
    Post: N' == N && immutable(N) && R = new Queue && Rn = floor(N / n) && forall i=1..Rn R[i] = a[n * i]
        getNth(int n)

    Pre: n > 0
    Post: N' == N - floor(N / n) && R = new Queue && Rn = floor(N / n) && forall i=1..Rn R[i] = a[n * i] &&
          forall i=1..N'  a'[i] == a[i + floor(i / n)]
        removeNth(int n)

    Pre: n > 0
    Post: N' == N - floor(N / n) && forall i=1..N'  a'[i] == a[i + floor(i / n)]
        dropNth(int n)

    Pre: true
    Post: n' == n && immutable(n) && R = new Queue && R.type == a.type
        getNewQueue()
 */
public interface Queue {
    // Pre: element != null
    // Post: n' = n + 1 && a[n'] == element && immutable(n)
    void enqueue(Object element);

    // Pre: n > 0
    // Post: R == a[n] && n' == n && immutable(n)
    Object element();

    // Pre: n > 0
    // Post: R == a[1] && n' == n - 1 && forall i=1..n': a'[i] == a[i + 1]
    Object dequeue();

    // Pre: true
    // Post: R == n && n' == n && immutable(n)
    int size();

    // Pre: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    boolean isEmpty();

    // Pre: true
    // Post: n' == 0
    void clear();

    // Pre: n > 0
    // Post: N' == N && immutable(N) && R = new Queue && Rn = floor(N / n) && forall i=1..Rn R[i] = a[n * i]
    Queue getNth(int n);

    // Pre: n > 0
    // Post: N' == N - floor(N / n) && R = new Queue && Rn = floor(N / n) && forall i=1..Rn R[i] = a[n * i] &&
    //      forall i=1..N'  a'[i] == a[i + floor(i / n)]
    Queue removeNth(int n);

    // Pre: n > 0
    // Post: N' == N - floor(N / n) && forall i=1..N'  a'[i] == a[i + floor(i / n)]
    void dropNth(int n);

    // Pre: true
    // Post: n' == n && immutable(n) && R = new Queue && R.type == a.type
    Queue getNewQueue();
}
