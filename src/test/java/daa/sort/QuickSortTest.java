package daa.sort;

import org.junit.jupiter.api.Test;
import daa.metrics.Metrics;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;


public class QuickSortTest {
    @Test void adversarialAndDepthBound() {
// adversarial: sorted input, duplicates
        int[] sorted = java.util.stream.IntStream.range(0, 10000).toArray();
        int[] dup = new int[20000];
        java.util.Arrays.fill(dup, 7);
        Metrics m1 = new Metrics(); QuickSort.sort(sorted, m1);
        Metrics m2 = new Metrics(); QuickSort.sort(dup, m2);
        assertTrue(isSorted(sorted) && isSorted(dup));
// randomized pivot + smaller-first ⇒ depth ~ O(log n)
        int n = 100000; int[] a = new Random(2).ints(n).toArray(); Metrics m = new Metrics();
        QuickSort.sort(a, m);
        assertTrue(m.maxRecDepth <= (2 * (int)Math.floor(Math.log(n)/Math.log(2))) + 20);
    }
    private static boolean isSorted(int[] x){ for(int i=1;i<x.length;i++) if(x[i]<x[i-1]) return false; return true; }
}