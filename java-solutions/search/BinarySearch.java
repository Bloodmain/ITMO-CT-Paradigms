package search;

public class BinarySearch {
    // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
    // (forall i1: 0 <= i1 <= l a[i1] > x) &&
    // (forall i2: r <= i2 < a.length a[i2] <= x) &&
    // r - l >= 1
    public static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
        // (forall i1: 0 <= i1 <= l a[i1] > x) &&
        // (forall i2: r <= i2 < a.length a[i2] <= x) &&
        // r - l >= 1
        if (r - l == 1) {
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l a[i1] > x) &&
            // (forall i2: r <= i2 < a.length a[i2] <= x) &&
            // r - l == 1
            // => (forall i1: 0 <= i1 < r a[i1] > x) &&
            // (forall i2: r <= i2 < a.length a[i2] <= x)
            return r;
        }
        int m = (r + l) / 2;
        // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
        // (forall i1: 0 <= i1 <= l a[i1] > x) &&
        // (forall i2: r <= i2 < a.length a[i2] <= x) &&
        // r - l > 1 &&
        // l < m < r
        int res;
        if (a[m] > x) {
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l a[i1] > x) &&
            // (forall i2: r <= i2 < a.length a[i2] <= x) &&
            // r - l > 1 &&
            // l < m < r &&
            // a[m] > x
            res = recursiveBinarySearch(x, a, m, r);
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l a[i1] > x) &&
            // (forall i2: r <= i2 < a.length a[i2] <= x) &&
            // r - l > 1 &&
            // l < m < r &&
            // a[m] > x &&
            // (forall i1: m <= i1 < res a[i1] > x) &&
            // (forall i2: res <= i2 < r a[i2] <= x)
            // => (forall i1: l <= i1 < res a[i1] > x) &&
            //    (forall i2: res <= i2 < r a[i2] <= x)
        } else {
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l a[i1] > x) &&
            // (forall i2: r <= i2 < a.length a[i2] <= x) &&
            // r - l > 1 &&
            // l < m < r &&
            // a[m] <= x
            res = recursiveBinarySearch(x, a, l, m);
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l a[i1] > x) &&
            // (forall i2: r <= i2 < a.length a[i2] <= x) &&
            // r - l > 1 &&
            // l < m < r &&
            // a[m] <= x &&
            // (forall i1: l <= i1 < res a[i1] > x) &&
            // (forall i2: res <= i2 < m a[i2] <= x)
            // => (forall i1: l <= i1 < res a[i1] > x) &&
            //    (forall i2: res <= i2 < r a[i2] <= x)
        }
        // (r - l) is decreasing on each iteration &&
        // (r - l) >= 1
        // => (r - l) will equal 1 => recursion will end
        return res;
    }
    // Post: (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
    // (forall i1: l <= i1 < res a[i1] > x) &&
    // (forall i2: res <= i2 < r a[i2] <= x)

    // Pre: forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]
    public static int iterativeBinarySearch(int x, int[] a) {
        // forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]
        int l = -1;
        int r = a.length;
        // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
        // l == -1 && r == a.length && l' == l && r' = r

        // Inv: (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
        //      (forall i1: 0 <= i1 <= l' a[i1] > x) &&
        //      (forall i2: r' <= i2 < a.length a[i2] <= x)
        while (r - l > 1) {
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l' a[i1] > x) &&
            // (forall i2: r' <= i2 < a.length a[i2] <= x) &&
            // r' - l' > 1
            //:NOTE: overflow
            int m = (r + l) / 2;
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l' a[i1] > x) &&
            // (forall i2: r' <= i2 < a.length a[i2] <= x) &&
            // r' - l' > 1 &&
            // l' < m < r'

            if (a[m] > x) {
                // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
                // (forall i1: 0 <= i1 <= l' a[i1] > x) &&
                // (forall i2: r' <= i2 < a.length a[i2] <= x) &&
                // r' - l' > 1 &&
                // l' < m < r' &&
                // a[m] > x
                l = m;
                // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
                // (forall i2: r' <= i2 < a.length a[i2] <= x) &&
                // a[m] > x && 
                // (forall i1: 0 <= i1 <= m a[i1] > x) &&
                // l' == m
                // => (forall i1: 0 <= i1 <= l' a[i1] > x) &&
            } else {
                // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
                // (forall i1: 0 <= i1 <= l' a[i1] > x) &&
                // (forall i2: r' <= i2 < a.length a[i2] <= x) &&
                // r' - l' > 1 &&
                // l' < m < r' &&
                // a[m] <= x
                r = m;
                // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
                // (forall i1: 0 <= i1 <= l' a[i1] > x) &&
                // a[m] <= x
                // (forall i2: m <= i2 < a.length a[i2] <= x) &&
                // r' == m
                // => (forall i2: r' <= i2 < a.length a[i2] <= x) &&
            }
            // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
            // (forall i1: 0 <= i1 <= l' a[i1] > x) &&
            // (forall i2: r' <= i2 < a.length a[i2] <= x) &&

            // (r - l) is decreasing on each iteration => the loop will end
        }
        // (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
        // (forall i1: 0 <= i1 <= l' a[i1] > x) &&
        // (forall i2: r' < i2 < a.length a[i2] <= x) &&
        // r' - l' == 1
        // => (forall i1: 0 <= i1 < r' a[i1] > x) &&
        // (forall i2: r' <= i2 < a.length a[i2] <= x)
        return r;
    }
    // Post: (forall i1: 0 <= i1 < a.length forall i2: i1 < i2 < a.length a[i1] >= a[i2]) &&
    // (forall i1: 0 <= i1 < r a[i1] > x) &&
    // (forall i2: r <= i2 < a.length a[i2] <= x)

    // Pre: args.length > 0 && (forall i: 0 <= i < args.length args[i] is int) &&
    // (forall i1: 1 <= i1 < args.length forall i2: i1 < i2 < args.length args[i1] >= args[i2])
    public static void main(String[] args) {
        // args.length > 0 && forall i: 0 <= i < args.length args[i] is int
        int x = Integer.parseInt(args[0]);
        // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0]
        int[] a = new int[args.length - 1];
        int i = 0;
        // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0] &&
        // i == 1 && a.length = args.length - 1

        // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0] &&
        // a.length = args.length - 1 &&
        // forall i': 0 <= i' < i a[i'] = args[i' + 1]
        while (i < args.length - 1) {
            // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0] &&
            // a.length = args.length - 1 &&
            // forall i': 0 <= i' < i a[i'] = args[i' + 1] &&
            // i < args.length - 1
            a[i] = Integer.parseInt(args[i + 1]);
            // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0] &&
            // a.length = args.length - 1 &&
            // forall i': 0 <= i' <= i a[i'] = args[i' + 1] &&
            // i < args.length - 1
            i++;
            // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0] &&
            // a.length = args.length - 1 &&
            // forall i': 0 <= i' < i a[i'] = args[i' + 1]
        }
        // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0] &&
        // forall i: 0 <= i < a.length a[i] = args[i + 1]
        int pos = iterativeBinarySearch(x, a);
        // args.length > 0 && forall i: 0 <= i < args.length args[i] is int && x == args[0] &&
        // forall i: 0 <= i < a.length a[i] = args[i + 1] &&
        // (forall i1: 0 <= i1 < pos a[i1] > x) &&
        // (forall i2: pos <= i2 < a.length a[i2] <= x)
        System.out.println(pos);
    }
    // args.length > 0 && (forall i: 0 <= i < args.length args[i] is int) &&
    // (forall i1: 1 <= i1 < args.length forall i2: i1 < i2 < args.length args[i1] >= args[i2])
}
