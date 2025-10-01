package daa.metrics;




public class Metrics {
    public long comparisons = 0;
    public long swaps = 0;
    public long allocations = 0;
    public int maxRecDepth = 0;


    private int currentDepth = 0;


    public void incDepth() { currentDepth++; maxRecDepth = Math.max(maxRecDepth, currentDepth); }
    public void decDepth() { currentDepth--; }
}