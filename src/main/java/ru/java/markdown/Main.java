package ru.java.markdown;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Specify a markdown file");
                return;
            }
            if (args.length > 1) {
                System.out.println("Too many command line arguments");
                return;
            }

            File inputFile = new File(args[0]);
            if (!inputFile.exists() || !inputFile.canRead()) {
                System.err.println("Unable to read the file");
                return;
            }

            TableOfContent toc = MarkdownTableOfContentGenerator.getTableOfContent(inputFile);

            if (!toc.isEmpty()) {
                System.out.println(toc);
            }
            Files.lines(Paths.get(args[0])).forEach(System.out::println);
            addTextToFileBeginning(toc.toString(), args[0]);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void addTextToFileBeginning(String text, String fileName) throws IOException {
        File source = new File(fileName);
        File tempFile = File.createTempFile("temp", ".txt");
        if (!source.renameTo(tempFile)) {
            throw new IOException("Failed to record text to the beginning of file");
        }
        copyFileWithNewBeginning(text, new File(fileName), tempFile);
        if (!tempFile.delete()) {
            throw new IOException("Failed to delete temporary file");
        }
    }

    private static void copyFileWithNewBeginning(String beginning, File dest, File source) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
             BufferedReader reader = new BufferedReader(new FileReader(source))
        ) {
            if (!beginning.isEmpty()) {
                writer.write(beginning);
                writer.write("\n");
            }
            while (reader.ready()) {
                writer.write(reader.readLine());
                writer.write("\n");
            }
        }
    }
}
