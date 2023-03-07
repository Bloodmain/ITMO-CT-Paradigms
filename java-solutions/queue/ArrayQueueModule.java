package queue;

import java.util.Arrays;
import java.util.Objects;

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

    Pre: element != null
    Post: n' = n + 1 && a'[0] == element && forall i=2..n': a'[i] = a[i-1]
        push()

    Pre: n > 0
    Post: R == a[n] && n' == n - 1 && immutable(n-1)
        remove()

    Pre: n > 0
    Post: R == a[n] && n' == n && immutable(n)
        peek()

    Pre: 1 <= i <= n
    Post: R == a[i] && n' == n && immutable(n)
        get()

    Pre: 1 <= i <= n && val != null
    Post: n' == n && a'[i] == val && forall i=j..n, j != i: a'[i] = a[i]
        set()
 */
public class ArrayQueueModule {
    private static int head = 0;
    private static int size = 0;
    private static Object[] queue = new Object[2];

    private static void ensureCapacity(final int capacity) {
        if (queue.length < capacity) {
            int newLength = Math.max(queue.length * 2, capacity);
            Object[] newQueue = new Object[newLength];
            for (int i = 0; i < size; ++i) {
                newQueue[i] = queue[head];
                head = modLength(++head);
            }
            queue = Arrays.copyOf(newQueue, newLength);
            head = 0;
        }
    }

    private static int modLength(final int pointer) {
        if (pointer >= queue.length) {
            return pointer - queue.length;
        } else if (pointer < 0) {
            return pointer + queue.length;
        } else {
            return pointer;
        }
    }

    private static int getTail() {
        return modLength(head + size);
    }

    // Pre: element != null
    // Post: n' = n + 1 && a'[n'] == element && immutable(n)
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        queue[getTail()] = element;
        size++;
    }

    // Pre: element != null
    // Post: n' = n + 1 && a'[0] == element && forall i=2..n': a'[i] = a[i-1]
    public static void push(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        head = modLength(--head);
        queue[head] = element;
        size++;
    }

    // Pre: n > 0
    // Post: R == a[1] && n' == n && immutable(n)
    public static Object element() {
        assert size > 0;
        return queue[head];
    }

    // Pre: n > 0
    // Post: R == a[n] && n' == n && immutable(n)
    public static Object peek() {
        assert size > 0;
        return queue[modLength(getTail() - 1)];
    }

    // Pre: n > 0
    // Post: R == a[1] && n' == n - 1 && forall i=1..n': a'[i] == a[i + 1]
    public static Object dequeue() {
        assert size > 0;
        Object res = queue[head];
        head = modLength(++head);
        size--;
        return res;
    }

    // Pre: n > 0
    // Post: R == a[n] && n' == n - 1 && immutable(n-1)
    public static Object remove() {
        assert size > 0;
        size--;
        return queue[getTail()];
    }

    // Pre: true
    // Post: R == n && n' == n && immutable(n)
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n' == 0
    public static void clear() {
        head = 0;
        size = 0;
    }

    // Pre: 1 <= i <= n
    // Post: R == a[i] && n' == n && immutable(n)
    public static Object get(final int i) {
        assert 0 <= i && i < size;
        return queue[modLength(head + i)];
    }

    // Pre: 1 <= i <= n && val != null
    // Post: n' == n && a'[i] == val && forall i=j..n, j != i: a'[i] = a[i]
    public static void set(final int i, final Object val) {
        assert 0 <= i && i < size;
        Objects.requireNonNull(val);

        queue[modLength(head + i)] = val;
    }
}
