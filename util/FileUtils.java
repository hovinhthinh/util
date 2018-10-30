package util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

public class FileUtils {
    public static ArrayList<String> getLines(File file, Charset charset) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            ArrayList<String> list = new ArrayList<>();
            String line = null;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
            list.trimToSize();
            return list;
        } catch (IOException e) {
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
        } catch (IOException e) {
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

        public LineStream(File file, Charset charset) throws IOException {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
