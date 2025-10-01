package daa.geometry;

import org.junit.jupiter.api.Test;
import daa.metrics.Metrics;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;


public class ClosestPairTest {
    @Test void smallNMatchesBruteforce() {
        Random r = new Random(4);
        for (int n = 2; n <= 2000; n += 199) {
            Point2D[] pts = new Point2D[n];
            for (int i = 0; i < n; i++) pts[i] = new Point2D(r.nextDouble(), r.nextDouble());
            Metrics m = new Metrics();
            double d = ClosestPair.solve(pts, m);
            double brute = brute(pts);
            assertEquals(brute, d, 1e-9);
        }
    }
    private static double brute(Point2D[] pts){
        double best = Double.POSITIVE_INFINITY;
        for (int i=0;i<pts.length;i++) for(int j=i+1;j<pts.length;j++)
            best = Math.min(best, Math.hypot(pts[i].x()-pts[j].x(), pts[i].y()-pts[j].y()));
        return best;
    }
}