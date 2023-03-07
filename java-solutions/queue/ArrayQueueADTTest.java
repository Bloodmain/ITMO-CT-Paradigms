package queue;

import java.util.*;

public class ArrayQueueADTTest {
    public static void error(String message) {
        System.err.println(message);
        System.exit(-1);
    }

    public static void testEmpty() {
        System.out.print("Testing empty: ");
        ArrayQueueADT queue = new ArrayQueueADT();
        ArrayQueueADT.clear(queue);
        assert ArrayQueueADT.isEmpty(queue) : "Empty queue returned false on isEmpty()";
        try {
            ArrayQueueADT.remove(queue);
            error("Error expected for removing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueADT.peek(queue);
            error("Error expected for peeking from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueADT.dequeue(queue);
            error("Error expected for dequeuing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueADT.element(queue);
            error("Error expected for elementing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueADT.get(queue, 0);
            error("Error expected for getting index 0 from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueADT.get(queue, 1);
            error("Error expected for getting index 1 from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueADT.set(queue, 2, "hey");
            error("Error expected for getting index 2 from empty queue");
        } catch (AssertionError ignored) {
        }

        ArrayQueueADT.clear(queue);
        assert ArrayQueueADT.isEmpty(queue) : "Empty queue returned false on isEmpty()";
        System.out.println("passed");
    }

    public static void testOneWay() {
        System.out.print("Testing one way adding: ");
        ArrayQueueADT queue = new ArrayQueueADT();
        for (int i = 0; i < 20; ++i) {
            ArrayQueueADT.enqueue(queue, i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = null;
            try {
                res = ArrayQueueADT.element(queue);
                assert res.equals(i);
                res = ArrayQueueADT.dequeue(queue);
                assert res.equals(i);
            } catch (AssertionError e) {
                error("Dequeuing/elementing isn't matching real element: got " + res + " | expected " + i);
            }
        }
        System.out.println("passed");
    }

    public static void testTwoWay() {
        System.out.print("Testing two way adding: ");
        ArrayQueueADT queue = new ArrayQueueADT();
        Deque<Integer> deq = new ArrayDeque<>();
        for (int i = 0; i < 20; ++i) {
            ArrayQueueADT.enqueue(queue, i);
            ArrayQueueADT.push(queue, 153 - i);
            deq.add(i);
            deq.addFirst(153 - i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = null, expected = null;
            try {
                res = ArrayQueueADT.element(queue);
                expected = deq.peekFirst();
                assert res.equals(expected);
                res = ArrayQueueADT.dequeue(queue);
                expected = deq.pollFirst();
                assert res.equals(expected);

                res = ArrayQueueADT.peek(queue);
                expected = deq.peekLast();
                assert res.equals(expected);
                res = ArrayQueueADT.remove(queue);
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
        ArrayQueueADT queue = new ArrayQueueADT();
        for (int i = 0; i < 20; ++i) {
            ArrayQueueADT.enqueue(queue, i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = ArrayQueueADT.get(queue, i);
            assert res.equals(i) :
                    "Getting (" + i + ") isn't matching real element: got " + res + " | expected " + i;

        }

        Object[] values = {213, -1, "Hey", "Stop", new ArrayList<>(), new Object[]{"genius"}};
        List<Integer> indexes = List.of(5, 0, 7, 19, 4, 7);
        for (int i = 0; i < values.length; ++i) {
            ArrayQueueADT.set(queue, indexes.get(i), values[i]);
        }
        for (int i = 0; i < 20; ++i) {
            Object expected = i;
            if (indexes.lastIndexOf(i) != -1) {
                expected = values[indexes.lastIndexOf(i)];
            }
            Object res = ArrayQueueADT.get(queue, i);
            assert res.equals(expected) :
                    "Getting (" + i + ") isn't matching real element: got " + res + " | expected " + expected;

        }
        System.out.println("passed");
    }

    public static void testPushingWithClearing() {
        System.out.print("Testing pushing with clearing: ");
        ArrayQueueADT queue = new ArrayQueueADT();
        Deque<Integer> deq = new ArrayDeque<>();
        Object res, expected;

        ArrayQueueADT.push(queue, 1);
        deq.addFirst(1);
        ArrayQueueADT.push(queue, 1);
        deq.addFirst(1);
        ArrayQueueADT.push(queue, 1);
        deq.addFirst(1);
        ArrayQueueADT.push(queue, 1);
        deq.addFirst(1);
        res = ArrayQueueADT.dequeue(queue);
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueADT.dequeue(queue);
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueADT.dequeue(queue);
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        ArrayQueueADT.enqueue(queue, 2);
        deq.addLast(2);
        ArrayQueueADT.enqueue(queue, 2);
        deq.addLast(2);

        res = ArrayQueueADT.remove(queue);
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueADT.remove(queue);
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueADT.remove(queue);
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        ArrayQueueADT.enqueue(queue, 3);
        deq.addLast(3);
        ArrayQueueADT.enqueue(queue, 3);
        deq.addLast(3);
        ArrayQueueADT.enqueue(queue, 3);
        deq.addLast(3);
        ArrayQueueADT.enqueue(queue, 3);
        deq.addLast(3);
        ArrayQueueADT.enqueue(queue, 3);
        deq.addLast(3);
        assert !ArrayQueueADT.isEmpty(queue) : "Not empty queue returned true on isEmpty()";
        ArrayQueueADT.clear(queue);
        deq.clear();
        assert ArrayQueueADT.isEmpty(queue) : "Empty queue returned false on isEmpty()";
        ArrayQueueADT.push(queue, 15);
        deq.addFirst(15);
        assert !ArrayQueueADT.isEmpty(queue) : "Not empty queue returned true on isEmpty()";

        res = ArrayQueueADT.peek(queue);
        expected = deq.peekLast();
        assert res.equals(expected) :
                "Peeked element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueADT.element(queue);
        expected = deq.peekFirst();
        assert res.equals(expected) :
                "Elemented element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueADT.remove(queue);
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;

        System.out.println("passed");
    }

    public static void testTwoQueues() {
        System.out.print("Testing two queues: ");
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();

        for (int i = 0; i < 20; ++i) {
            ArrayQueueADT.enqueue(queue1, i);
        }

        assert ArrayQueueADT.isEmpty(queue2) && ArrayQueueADT.size(queue2) == 0 :
                "Empty queue returned true on isEmpty()";

        for (int i = 1; i < 7; ++i) {
            ArrayQueueADT.push(queue2, 235 * i);
        }

        Object res = null;
        for (int i = 0; i < 13; ++i) {
            try {
                res = ArrayQueueADT.element(queue1);
                assert res.equals(i);
                res = ArrayQueueADT.dequeue(queue1);
                assert res.equals(i);
            } catch (AssertionError e) {
                error("Dequeuing/elementing isn't matching real element: got " + res + " | expected " + i);
            }
        }

        res = ArrayQueueADT.dequeue(queue2);
        assert res.equals(235 * 6) :
                "Dequeuing isn't matching real element: got " + res + " | expected " + 235 * 6;
        res = ArrayQueueADT.remove(queue2);
        assert res.equals(235) :
                "Removing isn't matching real element: got " + res + " | expected " + 235;
        System.out.println("passed");
    }

    public static void testRandom() {
        System.out.println("Test random: ");
        final int ITERATIONS = 100000000;
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        ArrayQueueADT queue3 = new ArrayQueueADT();
        ArrayQueueADT[] queues = {queue1, queue2, queue3};

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
            ArrayQueueADT queue = queues[queueIndex];
            Deque<Object> deq = dequeues.get(queueIndex);

            switch (command) {
                case "enqueue" -> {
                    Object el = rd.nextInt();
                    ArrayQueueADT.enqueue(queue, el);
                    deq.addLast(el);
                }
                case "element" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.peekFirst();
                    Object res = ArrayQueueADT.element(queue);
                    assert res.equals(expected) :
                            "Elemented element doesn't match real: got " + res + " | expected " + expected;
                }
                case "dequeue" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.pollFirst();
                    Object res = ArrayQueueADT.dequeue(queue);
                    assert res.equals(expected) :
                            "Dequeued element doesn't match real: got " + res + " | expected " + expected;
                }
                case "size" -> {
                    int expected = deq.size();
                    int res = ArrayQueueADT.size(queue);
                    assert res == expected :
                            "Size doesn't match real: got " + res + " | expected " + expected;
                }
                case "isEmpty" -> {
                    boolean expected = deq.isEmpty();
                    boolean res = ArrayQueueADT.isEmpty(queue);
                    assert res == expected :
                            "isEmpty() doesn't match real: got " + res + " | expected " + expected;
                }
                case "clear" -> {
                    deq.clear();
                    ArrayQueueADT.clear(queue);
                }
                case "push" -> {
                    Object el = rd.nextInt();
                    ArrayQueueADT.push(queue, el);
                    deq.addFirst(el);
                }
                case "remove" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.pollLast();
                    Object res = ArrayQueueADT.remove(queue);
                    assert res.equals(expected) :
                            "Removed element doesn't match real: got " + res + " | expected " + expected;
                }
                case "peek" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.peekLast();
                    Object res = ArrayQueueADT.peek(queue);
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
                    Object res = ArrayQueueADT.get(queue, ind);
                    assert res.equals(expected) :
                            "Peeked element doesn't match real: got " + res + " | expected " + expected;
                }
                case "set" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    int ind = rd.nextInt(deq.size());
                    Object el = rd.nextInt();
                    ArrayQueueADT.set(queue, ind, el);

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
