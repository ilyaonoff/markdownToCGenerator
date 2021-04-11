package ru.java.markdown;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownHeadingParser {
    public static void parseFile(File markdownFile, Consumer<Heading> consumer) throws ParserException {
        try (BufferedReader reader = new BufferedReader(new FileReader(markdownFile))) {

            boolean isCodeBlock = false;
            String previousLine = null;

            Pattern headingPattern = Pattern.compile("^(#+)\\s*(.*?)(\\s+#*\\s*)?$");
            final int groupOfHeadingLevel = 1;
            final int groupOfHeadingTitle = 2;

            Pattern headingPatternAlternativeSyntax = Pattern.compile("(=+|-+)");

            while (reader.ready()) {
                String line = reader.readLine().trim();

                if (line.startsWith("```")) {
                    isCodeBlock = !isCodeBlock;
                }

                if (isCodeBlock) {
                    continue;
                }
                Matcher matcher = headingPattern.matcher(line);
                Matcher matcherAlternativeSyntax = headingPatternAlternativeSyntax.matcher(line);

                Heading newHeading = new Heading();

                if (matcher.matches() && !matcher.group(groupOfHeadingTitle).isEmpty()) {
                    newHeading.setLevel(matcher.group(groupOfHeadingLevel).length());
                    newHeading.setName(matcher.group(groupOfHeadingTitle));
                    newHeading.setId(getId(matcher.group(groupOfHeadingTitle)));
                } else if (previousLine != null && !previousLine.isEmpty() && matcherAlternativeSyntax.matches()) {
                    newHeading.setLevel(line.startsWith("=") ? 1 : 2);
                    newHeading.setName(previousLine);
                    newHeading.setId(getId(previousLine));
                } else {
                    previousLine = line;
                    continue;
                }
                previousLine = null;

                consumer.accept(newHeading);
            }
        } catch (IOException e) {
            throw new ParserException("Errors with io", e);
        }
    }

    private static String getId(String name) {
        return name.toLowerCase().replaceAll("\\s", "-")
                                 .replaceAll("[^A-Za-z0-9-_]", "");
    }
}
