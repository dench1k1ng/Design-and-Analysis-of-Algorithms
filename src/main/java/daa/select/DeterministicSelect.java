package daa.select;

import daa.metrics.Metrics;
import daa.sort.InsertionSort;


public class DeterministicSelect {
    public static int select(int[] a, int k, Metrics m) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return select(a, 0, a.length - 1, k, m);
    }


    private static int select(int[] a, int lo, int hi, int k, Metrics m) {
        while (true) {
            if (lo == hi) return a[lo];
            int pivot = pivotByMediansOfFive(a, lo, hi, m);
            int[] part = partitionAroundPivot(a, lo, hi, pivot, m);
            int lt = part[0], gt = part[1]; // a[lo..lt-1] < pivot, a[lt..gt] == pivot
            if (k < lt) { hi = lt - 1; }
            else if (k > gt) { lo = gt + 1; }
            else return a[k];
        }
    }


    private static int pivotByMediansOfFive(int[] a, int lo, int hi, Metrics m) {
        int n = hi - lo + 1;
        if (n <= 5) {
            InsertionSort.sort(a, lo, hi, m);
            return a[lo + n/2];
        }
        int groups = (n + 4) / 5;
        for (int g = 0; g < groups; g++) {
            int gLo = lo + g*5;
            int gHi = Math.min(gLo + 4, hi);
            InsertionSort.sort(a, gLo, gHi, m);
            int medianIdx = gLo + (gHi - gLo)/2;
            swap(a, lo + g, medianIdx, m); // compact medians to front
        }
// recursively select median of medians in a[lo..lo+groups-1]
        return select(a, lo, lo + groups - 1, lo + groups/2, m);
    }


    private static int[] partitionAroundPivot(int[] a, int lo, int hi, int pivot, Metrics m) {
        int i = lo, lt = lo, gt = hi;
        while (i <= gt) {
            m.comparisons++;
            if (a[i] < pivot) swap(a, i++, lt++, m);
            else if (a[i] > pivot) swap(a, i, gt--, m);
            else i++;
        }
        return new int[]{lt, gt};
    }


    private static void swap(int[] a, int i, int j, Metrics m) {
        int t = a[i]; a[i] = a[j]; a[j] = t; m.swaps++;
    }
}