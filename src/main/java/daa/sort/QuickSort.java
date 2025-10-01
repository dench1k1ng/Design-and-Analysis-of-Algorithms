package daa.sort;

import daa.metrics.Metrics;


public class QuickSort {
    private static final int CUTOFF = 24;


    public static void sort(int[] a, Metrics m) {
        sort(a, 0, a.length - 1, m);
    }


    private static void sort(int[] a, int lo, int hi, Metrics m) {
        while (lo < hi) {
            if (hi - lo + 1 <= CUTOFF) {
                InsertionSort.sort(a, lo, hi, m); return;
            }
            int p = QuickPartition.partition(a, lo, hi, m);
            int leftSize = p - lo, rightSize = hi - p;
// Recurse on smaller side; iterate on larger — bounded stack depth
            if (leftSize < rightSize) {
                m.incDepth(); sort(a, lo, p - 1, m); m.decDepth();
                lo = p + 1; // tail-iteration on larger side
            } else {
                m.incDepth(); sort(a, p + 1, hi, m); m.decDepth();
                hi = p - 1;
            }
        }
    }
}