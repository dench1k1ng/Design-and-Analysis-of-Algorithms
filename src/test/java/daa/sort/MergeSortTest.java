package daa.sort;




import org.junit.jupiter.api.Test;
import daa.metrics.Metrics;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;


public class MergeSortTest {
    @Test void randomArrays() {
        Random r = new Random(1);
        for (int t = 0; t < 50; t++) {
            int n = 1 + r.nextInt(10_000);
            int[] a = r.ints(n).toArray();
            int[] b = a.clone();
            Metrics m = new Metrics();
            MergeSort.sort(a, m);
            java.util.Arrays.sort(b);
            assertArrayEquals(b, a);
        }
    }
}