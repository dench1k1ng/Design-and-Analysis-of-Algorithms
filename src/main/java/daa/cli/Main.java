package daa.cli;

import daa.metrics.CsvWriter;
import daa.metrics.Metrics;
import daa.metrics.Stopwatch;
import daa.sort.MergeSort;
import daa.sort.QuickSort;
import daa.select.DeterministicSelect;
import daa.geometry.ClosestPair;
import daa.geometry.Point2D;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        String mode = args.length > 0 ? args[0] : "sort"; // sort|select|closest|all
        int n = args.length > 1 ? Integer.parseInt(args[1]) : 100_000;
        Path out = Path.of(args.length > 2 ? args[2] : "out/metrics.csv");

        try (CsvWriter csv = new CsvWriter(out, "algo,n,nanos,comparisons,swaps,allocations,maxDepth")) {
            switch (mode) {
                case "sort" -> runSorts(n, csv);
                case "select" -> runSelect(n, csv);
                case "closest" -> runClosest(n, csv);
                case "all" -> {
                    runSorts(n, csv);
                    runSelect(n, csv);
                    runClosest(n, csv);
                }
                default -> throw new IllegalArgumentException("Unknown mode: " + mode);
            }
        }
    }

    private static void runSorts(int n, CsvWriter csv) throws Exception {
        int[] arr = randArray(n);

        // MergeSort
        int[] a1 = arr.clone();
        Metrics m1 = new Metrics();
        Stopwatch t1 = new Stopwatch();
        t1.start();
        MergeSort.sort(a1, m1);
        long ns1 = (long) (t1.elapsedMillis() * 1_000_000L);
        csv.row("mergesort", n, ns1, m1.comparisons, m1.swaps, m1.allocations, m1.maxRecDepth);

        // QuickSort
        int[] a2 = arr.clone();
        Metrics m2 = new Metrics();
        Stopwatch t2 = new Stopwatch();
        t2.start();
        QuickSort.sort(a2, m2);
        long ns2 = (long) (t2.elapsedMillis() * 1_000_000L);
        csv.row("quicksort", n, ns2, m2.comparisons, m2.swaps, m2.allocations, m2.maxRecDepth);

        // validate
        if (!isSorted(a1) || !isSorted(a2)) {
            throw new AssertionError("Sorting failed: arrays not sorted");
        }
    }

    private static void runSelect(int n, CsvWriter csv) throws Exception {
        int[] arr = randArray(n);
        int k = n / 2;
        Metrics m = new Metrics();
        Stopwatch t = new Stopwatch();
        t.start();
        int v = DeterministicSelect.select(arr, k, m);
        long ns = (long) (t.elapsedMillis() * 1_000_000L);

        // cross-check against sorting
        int[] b = arr.clone();
        Arrays.sort(b);
        if (b[k] != v) {
            throw new AssertionError("Select mismatch: expected " + b[k] + " but got " + v);
        }
        csv.row("select_MoM5", n, ns, m.comparisons, m.swaps, m.allocations, m.maxRecDepth);
    }

    private static void runClosest(int n, CsvWriter csv) throws Exception {
        Point2D[] pts = randPoints(n);
        Metrics m = new Metrics();
        Stopwatch t = new Stopwatch();
        t.start();
        double d = ClosestPair.solve(pts, m);
        long ns = (long) (t.elapsedMillis() * 1_000_000L);

        // spot-check on tiny n
        if (n <= 2000) {
            double brute = bruteForce(pts);
            if (Math.abs(brute - d) > 1e-9) {
                throw new AssertionError("ClosestPair mismatch: expected " + brute + " but got " + d);
            }
        }
        csv.row("closest_pair", n, ns, m.comparisons, m.swaps, m.allocations, m.maxRecDepth);
    }

    // ---- helpers ----

    private static int[] randArray(int n) {
        Random r = new Random(42);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt();
        return a;
    }

    private static Point2D[] randPoints(int n) {
        Random r = new Random(42);
        Point2D[] p = new Point2D[n];
        for (int i = 0; i < n; i++) p[i] = new Point2D(r.nextDouble(), r.nextDouble());
        return p;
    }

    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i] < a[i - 1]) return false;
        return true;
    }

    private static double bruteForce(Point2D[] pts) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                best = Math.min(best, Math.hypot(pts[i].x() - pts[j].x(), pts[i].y() - pts[j].y()));
            }
        }
        return best;
    }
}
