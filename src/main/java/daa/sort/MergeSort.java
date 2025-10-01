package daa.sort;

import daa.metrics.Metrics;


public class MergeSort {
    private static final int CUTOFF = 24; // small-n cutoff


    public static void sort(int[] a, Metrics m) {
        int[] buf = new int[a.length];
        m.allocations += a.length; // track reusable buffer alloc
        sort(a, 0, a.length - 1, buf, m);
    }


    private static void sort(int[] a, int lo, int hi, int[] buf, Metrics m) {
        if (hi - lo + 1 <= CUTOFF) {
            InsertionSort.sort(a, lo, hi, m);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        m.incDepth(); sort(a, lo, mid, buf, m); m.decDepth();
        m.incDepth(); sort(a, mid + 1, hi, buf, m); m.decDepth();
        if (a[mid] <= a[mid + 1]) return; // already ordered
        merge(a, lo, mid, hi, buf, m);
    }


    private static void merge(int[] a, int lo, int mid, int hi, int[] buf, Metrics m) {
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            m.comparisons++;
            buf[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        }
        while (i <= mid) buf[k++] = a[i++];
        while (j <= hi) buf[k++] = a[j++];
        for (k = lo; k <= hi; k++) a[k] = buf[k];
    }
}