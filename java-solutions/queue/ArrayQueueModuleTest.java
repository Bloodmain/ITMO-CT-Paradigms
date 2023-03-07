package queue;

import java.util.*;

public class ArrayQueueModuleTest {
    public static void error(String message) {
        System.err.println(message);
        System.exit(-1);
    }

    public static void testEmpty() {
        System.out.print("Testing empty: ");
        ArrayQueueModule.clear();
        assert ArrayQueueModule.isEmpty() : "Empty queue returned false on isEmpty()";
        try {
            ArrayQueueModule.remove();
            error("Error expected for removing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueModule.peek();
            error("Error expected for peeking from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueModule.dequeue();
            error("Error expected for dequeuing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueModule.element();
            error("Error expected for elementing from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueModule.get(0);
            error("Error expected for getting index 0 from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueModule.get(1);
            error("Error expected for getting index 1 from empty queue");
        } catch (AssertionError ignored) {
        }

        try {
            ArrayQueueModule.set(2, "hey");
            error("Error expected for getting index 2 from empty queue");
        } catch (AssertionError ignored) {
        }

        ArrayQueueModule.clear();
        assert ArrayQueueModule.isEmpty() : "Empty queue returned false on isEmpty()";
        System.out.println("passed");
    }

    public static void testOneWay() {
        System.out.print("Testing one way adding: ");
        ArrayQueueModule.clear();
        for (int i = 0; i < 20; ++i) {
            ArrayQueueModule.enqueue(i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = null;
            try {
                res = ArrayQueueModule.element();
                assert res.equals(i);
                res = ArrayQueueModule.dequeue();
                assert res.equals(i);
            } catch (AssertionError e) {
                error("Dequeuing/elementing isn't matching real element: got " + res + " | expected " + i);
            }
        }
        System.out.println("passed");
    }

    public static void testTwoWay() {
        System.out.print("Testing two way adding: ");
        ArrayQueueModule.clear();
        Deque<Integer> deq = new ArrayDeque<>();
        for (int i = 0; i < 20; ++i) {
            ArrayQueueModule.enqueue(i);
            ArrayQueueModule.push(153 - i);
            deq.add(i);
            deq.addFirst(153 - i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = null, expected = null;
            try {
                res = ArrayQueueModule.element();
                expected = deq.peekFirst();
                assert res.equals(expected);
                res = ArrayQueueModule.dequeue();
                expected = deq.pollFirst();
                assert res.equals(expected);

                res = ArrayQueueModule.peek();
                expected = deq.peekLast();
                assert res.equals(expected);
                res = ArrayQueueModule.remove();
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
        ArrayQueueModule.clear();
        for (int i = 0; i < 20; ++i) {
            ArrayQueueModule.enqueue(i);
        }
        for (int i = 0; i < 20; ++i) {
            Object res = ArrayQueueModule.get(i);
            assert res.equals(i) :
                    "Getting (" + i + ") isn't matching real element: got " + res + " | expected " + i;

        }

        Object[] values = {213, -1, "Hey", "Stop", new ArrayList<>(), new Object[]{"genius"}};
        List<Integer> indexes = List.of(5, 0, 7, 19, 4, 7);
        for (int i = 0; i < values.length; ++i) {
            ArrayQueueModule.set(indexes.get(i), values[i]);
        }
        for (int i = 0; i < 20; ++i) {
            Object expected = i;
            if (indexes.lastIndexOf(i) != -1) {
                expected = values[indexes.lastIndexOf(i)];
            }
            Object res = ArrayQueueModule.get(i);
            assert res.equals(expected) :
                    "Getting (" + i + ") isn't matching real element: got " + res + " | expected " + expected;

        }
        System.out.println("passed");
    }

    public static void testPushingWithClearing() {
        System.out.print("Testing pushing with clearing: ");
        ArrayQueueModule.clear();
        Deque<Integer> deq = new ArrayDeque<>();
        Object res, expected;

        ArrayQueueModule.push(1);
        deq.addFirst(1);
        ArrayQueueModule.push(1);
        deq.addFirst(1);
        ArrayQueueModule.push(1);
        deq.addFirst(1);
        ArrayQueueModule.push(1);
        deq.addFirst(1);
        res = ArrayQueueModule.dequeue();
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueModule.dequeue();
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueModule.dequeue();
        expected = deq.pollFirst();
        assert res.equals(expected) : "Dequeued element doesn't match real: got " + res + " | expected " + expected;
        ArrayQueueModule.enqueue(2);
        deq.addLast(2);
        ArrayQueueModule.enqueue(2);
        deq.addLast(2);

        res = ArrayQueueModule.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueModule.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueModule.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;
        ArrayQueueModule.enqueue(3);
        deq.addLast(3);
        ArrayQueueModule.enqueue(3);
        deq.addLast(3);
        ArrayQueueModule.enqueue(3);
        deq.addLast(3);
        ArrayQueueModule.enqueue(3);
        deq.addLast(3);
        ArrayQueueModule.enqueue(3);
        deq.addLast(3);
        assert !ArrayQueueModule.isEmpty() : "Not empty queue returned true on isEmpty()";
        ArrayQueueModule.clear();
        deq.clear();
        assert ArrayQueueModule.isEmpty() : "Empty queue returned false on isEmpty()";
        ArrayQueueModule.push(15);
        deq.addFirst(15);
        assert !ArrayQueueModule.isEmpty() : "Not empty queue returned true on isEmpty()";

        res = ArrayQueueModule.peek();
        expected = deq.peekLast();
        assert res.equals(expected) :
                "Peeked element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueModule.element();
        expected = deq.peekFirst();
        assert res.equals(expected) :
                "Elemented element doesn't match real: got " + res + " | expected " + expected;
        res = ArrayQueueModule.remove();
        expected = deq.pollLast();
        assert res.equals(expected) :
                "Removed element doesn't match real: got " + res + " | expected " + expected;

        System.out.println("passed");
    }

    public static void testRandom() {
        System.out.println("Test random: ");
        final int ITERATIONS = 100000000;
        ArrayQueueModule.clear();

        Random rd = new Random(
                1764211122204L + 129L * "sdgsdgsdg".hashCode() / "EnikiBeniki".hashCode()
        );

        String[] commands = new String[]{
                "enqueue", "element", "dequeue", "size", "isEmpty", "clear", "push", "remove",
                "peek", "get", "set"
        };

        Deque<Object> deq = new ArrayDeque<>();

        for (int i = 0; i < ITERATIONS; ++i) {
            if (i % 10000000 == 0) {
                System.out.println("Random " + i + "/" + ITERATIONS);
            }
            String command = commands[rd.nextInt(commands.length)];
            switch (command) {
                case "enqueue" -> {
                    Object el = rd.nextInt();
                    ArrayQueueModule.enqueue(el);
                    deq.addLast(el);
                }
                case "element" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.peekFirst();
                    Object res = ArrayQueueModule.element();
                    assert res.equals(expected) :
                            "Elemented element doesn't match real: got " + res + " | expected " + expected;
                }
                case "dequeue" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.pollFirst();
                    Object res = ArrayQueueModule.dequeue();
                    assert res.equals(expected) :
                            "Dequeued element doesn't match real: got " + res + " | expected " + expected;
                }
                case "size" -> {
                    int expected = deq.size();
                    int res = ArrayQueueModule.size();
                    assert res == expected :
                            "Size doesn't match real: got " + res + " | expected " + expected;
                }
                case "isEmpty" -> {
                    boolean expected = deq.isEmpty();
                    boolean res = ArrayQueueModule.isEmpty();
                    assert res == expected :
                            "isEmpty() doesn't match real: got " + res + " | expected " + expected;
                }
                case "clear" -> {
                    deq.clear();
                    ArrayQueueModule.clear();
                }
                case "push" -> {
                    Object el = rd.nextInt();
                    ArrayQueueModule.push(el);
                    deq.addFirst(el);
                }
                case "remove" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.pollLast();
                    Object res = ArrayQueueModule.remove();
                    assert res.equals(expected) :
                            "Removed element doesn't match real: got " + res + " | expected " + expected;
                }
                case "peek" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    Object expected = deq.peekLast();
                    Object res = ArrayQueueModule.peek();
                    assert res.equals(expected) :
                            "Peeked element doesn't match real: got " + res + " | expected " + expected;
                }
                case "get" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    int ind = rd.nextInt(deq.size());
                    List<Object> savedObjects = new ArrayList<>();
                    for (int j=0;j<ind;++j) {
                        savedObjects.add(deq.pollFirst());
                    }
                    Object expected = deq.peekFirst();
                    for (int j=savedObjects.size()-1;j>=0;--j) {
                        deq.addFirst(savedObjects.get(j));
                    }
                    Object res = ArrayQueueModule.get(ind);
                    assert res.equals(expected) :
                            "Peeked element doesn't match real: got " + res + " | expected " + expected;
                }
                case "set" -> {
                    if (deq.size() == 0) {
                        break;
                    }
                    int ind = rd.nextInt(deq.size());
                    Object el = rd.nextInt();
                    ArrayQueueModule.set(ind, el);

                    List<Object> savedObjects = new ArrayList<>();
                    for (int j=0;j<ind;++j) {
                        savedObjects.add(deq.pollFirst());
                    }
                    deq.pollFirst();
                    deq.addFirst(el);
                    for (int j=savedObjects.size()-1;j>=0;--j) {
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
        testRandom();
    }
}
