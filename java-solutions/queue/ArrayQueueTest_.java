package queue;

import java.util.*;

public class ArrayQueueTest_ {
    public static void error(String message) {
        System.err.println(message);
        System.exit(-1);
    }

    public static void testEmpty() {
        System.out.print("Testing empty: ");
        ArrayQueue queue = new ArrayQueue();
        queue.clear();
        assert queue.isEmpty() : "Empty queue returned false on isEmpty()";
        try {
            queue.remove();
            error("Error expected for removing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            queue.peek();
            error("Error expected for peeking from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            queue.dequeue();
            error("Error expected for dequeuing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            queue.element();
            error("Error expected for elementing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            queue.get(0);
            error("Error expected for getting index 0 from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            queue.get(1);
            error("Error expected for getting index 1 from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            queue.set(2, "hey");
            error("Error expected for getting index 2 from empty queue");
        } catch (AssertionError ignored) {
        }

        queue.clear();
        assert queue.isEmpty() : "Empty queue returned false on isEmpty()";
        System.out.println("passed");
    }

    public static void testOneWay() {
        System.out.print("Testing one way adding: ");
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < 20; ++i) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = null;
            try {
                res = queue.element();
                assert res.equals(i);
                res = queue.dequeue();
                assert res.equals(i);
            } catch (AssertionError e) {
                error("Dequeuing/elementing isn't matching real element: got " + res + " | expected " + i);
            }
        }
        System.out.println("passed");
    }

    public static void testTwoWay() {
        System.out.print("Testing two way adding: ");
        ArrayQueue queue = new ArrayQueue();
        Deque<Integer> deq = new ArrayDeque<>();
        for (int i = 0; i < 20; ++i) {
            queue.enqueue(i);
            queue.push(153 - i);
            deq.add(i);
            deq.addFirst(153 - i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = null, expected = null;
            try {
                res = queue.element();
                expected = deq.peekFirst();
                assert res.equals(expected);
                res = queue.dequeue();
                expected = deq.pollFirst();
                assert res.equals(expected);

                res = queue.peek();
                expected = deq.peekLast();
                assert res.equals(expected);
                res = queue.remove();
                expected = deq.pollLast();
                assert res.equals(expected);
            } catch (AssertionError e) {
                error("Returned element isn't matching real element: got " + res + " | expected " + expected);
            }
        }
        System.out.println("passed");
    }

    public static void testSetGet() {
        System.out.print("Testing set/get: ");
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < 20; ++i) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = queue.get(i);
            assert res.equals(i) :
                    "Getting (" + i + ") isn't matching real element: got " + res + " | expected " + i;

        }

        Object[] values = {213, -1, "Hey", "Stop", new ArrayList<>(), new Object[]{"genius"}};
        List<Integer> indexes = List.of(5, 0, 7, 19, 4, 7);
        for (int i = 0; i < values.length; ++i) {
            queue.set(indexes.get(i), values[i]);
        }
        for (int i = 0; i < 20; ++i) {
            Object expected = i;
            if (indexes.lastIndexOf(i) != -1) {
                expected = values[indexes.lastIndexOf(i)];
            }
            Object res = queue.get(i);
            assert res.equals(expected) :
                    "Getting (" + i + ") isn't matching real element: got " + res + " | expected " + expected;

        }
        System.out.println("passed");
    }

    public static void testPushingWithClearing() {
        System.out.print("Testing pushing with clearing: ");
        ArrayQueue queue = new ArrayQueue();
        Deque<Integer> deq = new ArrayDeque<>();
        Object res, expected;

        queue.push(1);
        deq.addFirst(1);
        queue.push(1);
        deq.addFirst(1);
        queue.push(1);
        deq.addFirst(1);
        queue.push(1);
        deq.addFirst(1);
        res = queue.dequeue();
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        res = queue.dequeue();
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        res = queue.dequeue();
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        queue.enqueue(2);
        deq.addLast(2);
        queue.enqueue(2);
        deq.addLast(2);

        res = queue.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        res = queue.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        res = queue.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        queue.enqueue(3);
        deq.addLast(3);
        queue.enqueue(3);
        deq.addLast(3);
        queue.enqueue(3);
        deq.addLast(3);
        queue.enqueue(3);
        deq.addLast(3);
        queue.enqueue(3);
        deq.addLast(3);
        assert !queue.isEmpty() : "Not empty queue returned true on isEmpty()";
        queue.clear();
        deq.clear();
        assert queue.isEmpty() : "Empty queue returned false on isEmpty()";
        queue.push(15);
        deq.addFirst(15);
        assert !queue.isEmpty() : "Not empty queue returned true on isEmpty()";

        res = queue.peek();
        expected = deq.peekLast();
        assert res.equals(expected) :
                "Peeked element doesn't match real: got " + res + " | expected " + expected;
        res = queue.element();
        expected = deq.peekFirst();
        assert res.equals(expected) :
                "Elemented element doesn't match real: got " + res + " | expected " + expected;
        res = queue.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;

        System.out.println("passed");
    }

    public static void testTwoQueues() {
        System.out.print("Testing two queues: ");
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();

        for (int i = 0; i < 20; ++i) {
            queue1.enqueue(i);
        }

        assert queue2.isEmpty() && queue2.size() == 0 :
                "Empty queue returned true on isEmpty()";

        for (int i = 1; i < 7; ++i) {
            queue2.push(235 * i);
        }

        Object res = null;
        for (int i = 0; i < 13; ++i) {
            try {
                res = queue1.element();
                assert res.equals(i);
                res = queue1.dequeue();
                assert res.equals(i);
            } catch (AssertionError e) {
                error("Dequeuing/elementing isn't matching real element: got " + res + " | expected " + i);
            }
        }

        res = queue2.dequeue();
        assert res.equals(235 * 6) :
                "Dequeuing isn't matching real element: got " + res + " | expected " + 235 * 6;
        res = queue2.remove();
        assert res.equals(235) :
                "Removing isn't matching real element: got " + res + " | expected " + 235;
        System.out.println("passed");
    }

    public static void testRandom() {
        System.out.println("Test random: ");
        final int ITERATIONS = 100000000;
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        ArrayQueue queue3 = new ArrayQueue();
        ArrayQueue[] queues = {queue1, queue2, queue3};

        Random rd = new Random(
                1764211122204L + 129L * "sdgsdgsdg".hashCode() / "EnikiBeniki".hashCode()
        );

        String[] commands = new String[]{
                "enqueue", "element", "dequeue", "size", "isEmpty", "clear", "push", "remove",
                "peek", "get", "set"
        };

        Deque<Object> deq1 = new ArrayDeque<>();
        Deque<Object> deq2 = new ArrayDeque<>();
        Deque<Object> deq3 = new ArrayDeque<>();
        List<Deque<Object>> dequeues = List.of(deq1, deq2, deq3);

        for (int i = 0; i < ITERATIONS; ++i) {
            if (i % 10000000 == 0) {
                System.out.println("Random " + i + "/" + ITERATIONS);
            }
            String command = commands[rd.nextInt(commands.length)];
            int queueIndex = rd.nextInt(3);
            ArrayQueue queue = queues[queueIndex];
            Deque<Object> deq = dequeues.get(queueIndex);

            switch (command) {
                case "enqueue" -> {
                    Object el = rd.nextInt();
                    queue.enqueue(el);
                    deq.addLast(el);
                }
                case "element" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.peekFirst();
                    Object res = queue.element();
                    assert res.equals(expected) :
                            "Elemented element doesn't match real: got " + res + " | expected " + expected;
                }
                case "dequeue" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.pollFirst();
                    Object res = queue.dequeue();
                    assert res.equals(expected) :
                            "Dequeued element doesn't match real: got " + res + " | expected " + expected;
                }
                case "size" -> {
                    int expected = deq.size();
                    int res = queue.size();
                    assert res == expected :
                            "Size doesn't match real: got " + res + " | expected " + expected;
                }
                case "isEmpty" -> {
                    boolean expected = deq.isEmpty();
                    boolean res = queue.isEmpty();
                    assert res == expected :
                            "isEmpty() doesn't match real: got " + res + " | expected " + expected;
                }
                case "clear" -> {
                    deq.clear();
                    queue.clear();
                }
                case "push" -> {
                    Object el = rd.nextInt();
                    queue.push(el);
                    deq.addFirst(el);
                }
                case "remove" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.pollLast();
                    Object res = queue.remove();
                    assert res.equals(expected) :
                            "Removed element doesn't match real: got " + res + " | expected " + expected;
                }
                case "peek" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.peekLast();
                    Object res = queue.peek();
                    assert res.equals(expected) :
                            "Peeked element doesn't match real: got " + res + " | expected " + expected;
                }
                case "get" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    int ind = rd.nextInt(deq.size());
                    List<Object> savedObjects = new ArrayList<>();
                    for (int j = 0; j < ind; ++j) {
                        savedObjects.add(deq.pollFirst());
                    }
                    Object expected = deq.peekFirst();
                    for (int j = savedObjects.size() - 1; j >= 0; --j) {
                        deq.addFirst(savedObjects.get(j));
                    }
                    Object res = queue.get(ind);
                    assert res.equals(expected) :
                            "Peeked element doesn't match real: got " + res + " | expected " + expected;
                }
                case "set" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    int ind = rd.nextInt(deq.size());
                    Object el = rd.nextInt();
                    queue.set(ind, el);

                    List<Object> savedObjects = new ArrayList<>();
                    for (int j = 0; j < ind; ++j) {
                        savedObjects.add(deq.pollFirst());
                    }
                    deq.pollFirst();
                    deq.addFirst(el);
                    for (int j = savedObjects.size() - 1; j >= 0; --j) {
                        deq.addFirst(savedObjects.get(j));
                    }
                }
            }
        }
        System.out.println("Passed");
    }

    public static void main(String[] args) {
        try {
            assert false;
            error("Enable asserts to run tests.");
        } catch (AssertionError ignored) {
        }
        testEmpty();
        testOneWay();
        testTwoWay();
        testSetGet();
        testPushingWithClearing();
        testTwoQueues();
        testRandom();
    }
}
