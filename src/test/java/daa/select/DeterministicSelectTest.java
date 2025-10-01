package daa.select;

import org.junit.jupiter.api.Test;
import daa.metrics.Metrics;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


public class DeterministicSelectTest {
    @Test void compareAgainstSort() {
        Random r = new Random(3);
        for (int t = 0; t < 100; t++) {
            int n = 1 + r.nextInt(50_000);
            int[] a = r.ints(n).toArray();
            int k = r.nextInt(n);
            int[] b = a.clone(); Arrays.sort(b);
            int v = DeterministicSelect.select(a, k, new Metrics());
            assertEquals(b[k], v);
        }
    }
}