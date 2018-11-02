package util;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

public class FileUtils {
    private static InputStream getFileDecodedStream(File file) throws IOException, CompressorException {
        int dot = file.getName().lastIndexOf(".");
        String extension = dot == -1 ? null : file.getName().substring(dot + 1).toLowerCase();
        if (extension == null) {
            return new FileInputStream(file);
        }
        String compressorName;
        switch (extension) {
            case "gz":
                compressorName = CompressorStreamFactory.GZIP;
                break;
            // More extensions here.
            default:
                compressorName = null;
        }
        return compressorName == null ? new FileInputStream(file) :
                new CompressorStreamFactory().createCompressorInputStream(compressorName, new FileInputStream(file));
    }

    public static ArrayList<String> getLines(File file, Charset charset) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getFileDecodedStream(file), charset))) {
            ArrayList<String> list = new ArrayList<>();
            String line = null;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
            list.trimToSize();
            return list;
        } catch (IOException | CompressorException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getLines(String file) {
        return getLines(new File(file), Charset.defaultCharset());
    }

    public static ArrayList<String> getLines(String file, String charset) {
        return getLines(new File(file), Charset.forName(charset));
    }

    public static LineStream getLineStream(File file, Charset charset) {
        try {
            return new LineStream(file, charset);
        } catch (IOException | CompressorException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LineStream getLineStream(String file) {
        return getLineStream(new File(file), Charset.defaultCharset());
    }

    public static LineStream getLineStream(String file, String charset) {
        return getLineStream(new File(file), Charset.forName(charset));
    }

    public static class LineStream implements Iterable<String> {

        private BufferedReader in = null;

        public LineStream(File file, Charset charset) throws IOException, CompressorException {
            in = new BufferedReader(new InputStreamReader(getFileDecodedStream(file), charset));
        }

        public String readLine() {
            try {
                return in == null ? null : in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                private String currentLine = null;

                @Override
                public boolean hasNext() {
                    try {
                        return in != null && (currentLine = in.readLine()) != null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                public String next() {
                    return currentLine;
                }
            };
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // Do nothing.
            }
            super.finalize();
        }
    }
}
