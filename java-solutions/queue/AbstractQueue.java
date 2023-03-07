package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    @Override
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        size++;
        enqueueImpl(element);
    }

    @Override
    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    @Override
    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    private Object[] getBuffer() {
        Object[] buffer = new Object[size()];
        int i = 0;
        while (!isEmpty()) {
            buffer[i] = dequeue();
            i++;
        }
        return buffer;
    }

    @Override
    public void dropNth(int n) {
        int size1 = size();
        for (int i = 0; i < size1; ++i) {
            Object element = dequeue();
            if ((i + 1) % n != 0) {
                enqueue(element);
            }
        }
    }

    @Override
    public Queue getNth(int n) {
        Queue res = getNewQueue();
        Object[] buffer = getBuffer();
        for (int i = 0; i < buffer.length; ++i) {
            if ((i + 1) % n == 0) {
                res.enqueue(buffer[i]);
            }
            enqueue(buffer[i]);
        }
        return res;
    }

    @Override
    public Queue removeNth(int n) {
        Queue res = getNth(n);
        dropNth(n);
        return res;
    }

    protected abstract Object dequeueImpl();

    protected abstract Object elementImpl();

    protected abstract void clearImpl();

    protected abstract void enqueueImpl(Object element);
}
