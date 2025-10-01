package daa.geometry;



import daa.metrics.Metrics;
import java.util.*;


public class ClosestPair {
    public static double solve(Point2D[] pts, Metrics m) {
        Point2D[] px = pts.clone();
        Arrays.sort(px, Comparator.comparingDouble(Point2D::x));
        m.allocations += px.length;
        Point2D[] py = px.clone();
        Arrays.sort(py, Comparator.comparingDouble(Point2D::y));
        m.allocations += py.length;
        return rec(px, py, 0, px.length - 1, m);
    }


    private static double rec(Point2D[] px, Point2D[] py, int lo, int hi, Metrics m) {
        int n = hi - lo + 1;
        if (n <= 3) {
            double best = Double.POSITIVE_INFINITY;
            for (int i = lo; i <= hi; i++)
                for (int j = i + 1; j <= hi; j++) {
                    m.comparisons++;
                    best = Math.min(best, dist(px[i], px[j]));
                }
            return best;
        }
        int mid = lo + n/2;
        Point2D midPt = px[mid];
// split py into left/right by x
        List<Point2D> pyl = new ArrayList<>(n);
        List<Point2D> pyr = new ArrayList<>(n);
        for (Point2D p : py) {
            if (p.x() <= midPt.x()) pyl.add(p); else pyr.add(p);
        }
        m.allocations += pyl.size() + pyr.size();
        m.incDepth(); double dl = rec(px, pyl.toArray(Point2D[]::new), lo, mid, m); m.decDepth();
        m.incDepth(); double dr = rec(px, pyr.toArray(Point2D[]::new), mid + 1, hi, m); m.decDepth();
        double d = Math.min(dl, dr);
// strip within d of mid x
        List<Point2D> strip = new ArrayList<>();
        for (Point2D p : py) if (Math.abs(p.x() - midPt.x()) < d) strip.add(p);
        m.allocations += strip.size();
        double best = d;
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < Math.min(strip.size(), i + 8); j++) {
                m.comparisons++;
                best = Math.min(best, dist(strip.get(i), strip.get(j)));
            }
        }
        return best;
    }


    private static double dist(Point2D a, Point2D b) {
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        return Math.hypot(dx, dy);
    }
}