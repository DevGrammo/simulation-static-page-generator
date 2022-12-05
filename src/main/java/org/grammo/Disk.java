package org.grammo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Disk {
    public static void toDisk(String path, String text) {
        try {
            java.io.FileWriter myWriter = new java.io.FileWriter(path);
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to file: " + path);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static class WriteThread extends Thread {
        private final String path;
        private final String text;

        public WriteThread(String path, String text) {
            this.path = path;
            this.text = text;
        }

        public void run() {
            System.out.println("writer thread: " + Thread.currentThread().getName());
            toDisk(path, text);
        }
    }


    public static String readContentPreamble(String contentPath) throws IOException {
        // Reads preamble from md file. Throws error if invalid preamble.
        // Returns a map of object key - value pairs
        String delimiter = "---";
        BufferedReader reader = new BufferedReader(new FileReader(contentPath));
        String firstLine = reader.readLine();

        if (!firstLine.equals(delimiter)) {
            throw new RuntimeException("Invalid preamble.");
        }
        String preambleLine = reader.readLine();
        StringBuilder preambleText = new StringBuilder(1000);
        while (!preambleLine.equals(delimiter)) {
            preambleText.append(preambleLine).append("\n");
            preambleLine = reader.readLine();
            if (preambleLine == null) {
                throw new RuntimeException("Invalid preamble.");
            }
        }
        reader.close();
        return preambleText.toString();
    }

    public static String readFile(String filePath) throws FileNotFoundException {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);

        return new BufferedReader(
                new InputStreamReader(targetStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

    }

}
