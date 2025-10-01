package daa.metrics;



import java.io.*;
import java.nio.file.*;


public class CsvWriter implements Closeable {
    private final BufferedWriter bw;
    public CsvWriter(Path path, String header) throws IOException {
        Files.createDirectories(path.getParent());
        bw = Files.newBufferedWriter(path);
        bw.write(header); bw.newLine();
    }
    public void row(Object... cells) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(cells[i]);
        }
        bw.write(sb.toString()); bw.newLine();
    }
    @Override public void close() throws IOException { bw.close(); }
}