package daa.sort;

import daa.metrics.Metrics;
import java.util.concurrent.ThreadLocalRandom;


public class QuickPartition {
    public static int partition(int[] a, int lo, int hi, Metrics m) {
        int pivotIdx = ThreadLocalRandom.current().nextInt(lo, hi + 1);
        swap(a, lo, pivotIdx, m);
        int pivot = a[lo];
        int i = lo + 1, j = hi;
        while (true) {
            while (i <= hi && a[i] < pivot) { m.comparisons++; i++; }
            while (j >= lo + 1 && a[j] > pivot) { m.comparisons++; j--; }
            if (i >= j) break;
            swap(a, i++, j--, m);
        }
        swap(a, lo, j, m);
        return j;
    }


    public static void swap(int[] a, int i, int j, Metrics m) {
        int tmp = a[i]; a[i] = a[j]; a[j] = tmp; m.swaps++;
    }
}