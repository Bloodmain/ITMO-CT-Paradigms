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
public class ArrayQueueADT {
    private int head;
    private int size;
    private Object[] queue;

    public ArrayQueueADT() {
        head = 0;
        size = 0;
        queue = new Object[2];
    }

    private static void ensureCapacity(final ArrayQueueADT queueADT, final int capacity) {
        if (queueADT.queue.length < capacity) {
            int newLength = Math.max(queueADT.queue.length * 2, capacity);
            Object[] newQueue = new Object[newLength];
            for (int i = 0; i < queueADT.size; ++i) {
                newQueue[i] = queueADT.queue[queueADT.head];
                queueADT.head = modLength(queueADT, ++queueADT.head);
            }
            queueADT.queue = Arrays.copyOf(newQueue, newLength);
            queueADT.head = 0;
        }
    }

    private static int modLength(final ArrayQueueADT queueADT, final int pointer) {
        if (pointer >= queueADT.queue.length) {
            return pointer - queueADT.queue.length;
        } else if (pointer < 0) {
            return pointer + queueADT.queue.length;
        } else {
            return pointer;
        }
    }

    private static int getTail(final ArrayQueueADT queueADT) {
        return modLength(queueADT, queueADT.head + queueADT.size);
    }

    // Pre: element != null
    // Post: n' = n + 1 && a'[n'] == element && immutable(n)
    public static void enqueue(final ArrayQueueADT queueADT, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queueADT, queueADT.size + 1);
        queueADT.queue[getTail(queueADT)] = element;
        queueADT.size++;
    }

    // Pre: n > 0
    // Post: R == a[1] && n' == n && immutable(n)
    public static Object element(final ArrayQueueADT queueADT) {
        assert queueADT.size > 0;
        return queueADT.queue[queueADT.head];
    }

    // Pre: element != null
    // Post: n' = n + 1 && a'[0] == element && forall i=2..n': a'[i] = a[i-1]
    public static void push(final ArrayQueueADT queueADT, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queueADT, queueADT.size + 1);
        queueADT.head = modLength(queueADT, --queueADT.head);
        queueADT.queue[queueADT.head] = element;
        queueADT.size++;
    }

    // Pre: n > 0
    // Post: R == a[n] && n' == n && immutable(n)
    public static Object peek(final ArrayQueueADT queueADT) {
        assert queueADT.size > 0;
        return queueADT.queue[modLength(queueADT, getTail(queueADT) - 1)];
    }

    // Pre: n > 0
    // Post: R == a[n] && n' == n - 1 && immutable(n-1)
    public static Object remove(final ArrayQueueADT queueADT) {
        assert queueADT.size > 0;
        queueADT.size--;
        return queueADT.queue[getTail(queueADT)];
    }

    // Pre: 1 <= i <= n
    // Post: R == a[i] && n' == n && immutable(n)
    public static Object get(final ArrayQueueADT queueADT, final int i) {
        assert 0 <= i && i < queueADT.size;
        return queueADT.queue[modLength(queueADT, queueADT.head + i)];
    }

    // Pre: 1 <= i <= n && val != null
    // Post: n' == n && a'[i] == val && forall i=j..n, j != i: a'[i] = a[i]
    public static void set(final ArrayQueueADT queueADT, final int i, final Object val) {
        assert 0 <= i && i < queueADT.size;
        Objects.requireNonNull(val);

        queueADT.queue[modLength(queueADT, queueADT.head + i)] = val;
    }

    // Pre: n > 0
    // Post: R == a[1] && n' == n - 1 && forall i=1..n': a'[i] == a[i + 1]
    public static Object dequeue(final ArrayQueueADT queueADT) {
        assert queueADT.size > 0;
        Object res = queueADT.queue[queueADT.head];
        queueADT.head = modLength(queueADT, ++queueADT.head);
        queueADT.size--;
        return res;
    }

    // Pre: true
    // Post: R == n && n' == n && immutable(n)
    public static int size(final ArrayQueueADT queueADT) {
        return queueADT.size;
    }

    // Pre: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty(final ArrayQueueADT queueADT) {
        return queueADT.size == 0;
    }

    // Pre: true
    // Post: n' == 0
    public static void clear(final ArrayQueueADT queueADT) {
        queueADT.head = 0;
        queueADT.size = 0;
    }
}
