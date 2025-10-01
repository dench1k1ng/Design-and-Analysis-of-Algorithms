package daa.metrics;




public class Stopwatch {
    private long start;
    public void start() { start = System.nanoTime(); }
    public long elapsedNanos() { return System.nanoTime() - start; }
    public double elapsedMillis() { return elapsedNanos() / 1_000_000.0; }
}