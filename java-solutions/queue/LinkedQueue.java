package queue;

import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private Node next;
        private final Object val;

        public Node(Object val, Node next) {
            this.val = Objects.requireNonNull(val);
            this.next = next;
        }
    }

    private Node head;
    private Node tail;

    @Override
    protected void enqueueImpl(Object element) {
        if (size == 1) {
            tail = new Node(element, null);
            head = tail;
        } else {
            tail.next = new Node(element, null);
            tail = tail.next;
        }
    }

    @Override
    protected Object elementImpl() {
        return head.val;
    }

    @Override
    protected Object dequeueImpl() {
        Object res = head.val;
        head = head.next;
        return res;
    }

    @Override
    protected void clearImpl() {
        head = null;
        tail = null;
    }

    @Override
    public Queue getNewQueue() {
        return new LinkedQueue();
    }
}
